package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;


/**
 * Solution for Day 9 of Advent of Code 2024 - Disk Fragmenter
 * Problem description: <a href="https://adventofcode.com/2024/day/9">...</a>
 *
 * The approach -
 * 1. Read the input from the file and store the physical disk and the files in a list. The physical disk is represented
 * by a list of Optional Long values. The list index is a block identifier, and the value is the file identifier if the
 * block is occupied by a file. Otherwise, the value is empty. The files are represented by a list of Pair values where
 * the first value is the file identifier, and the second value is a list of block identifiers occupied by the file.
 * Important to note that the block identifier is the index in the physical disk list.
 * 2. For part 1, we need to defragment the disk by only considering whether an individual block is occupied by a file.
 * We start by finding the first free block and the last non-free block. We swap the contents of the first free block
 * with the last non-free block and repeat this process until the first free block is less than the last non-free block.
 * 3. For part 2, we need to defragment the disk by considering whole files, i.e. contiguous sequences of block identifiers,
 * and contiguous sequences of blocks that are not occupied by files. We do this by finding the first free block contiguous
 * sequence from the beginning of the physical disk, that can accommodate the file blocks, and move the first file from
 * the end to this sequence. We move to the next file from the end and move it the same way until all files are moved.
 *
 * Complexity and Runtimes:
 * The time complexity for both parts 1 and 2 is O(N * B), where:
 *    N is the number of files
 *    B is the number of blocks in the disk
 *
 * Runtimes on MacBook Pro M3:
 *   Part 1: 13ms
 *   Part 2: 211ms
 *
 * Notes:
 * 1. It was real fun to find the most suitable data structures to represent the physical disk and the files. It took
 * a couple of iterations to arrive at the data structures in the SolutionInput
 * 2. One thing to note here is that the Solution input files list will have wrong information after part2 is run because
 * the files are moved to the contiguous free space. The files list is not needed after part2 is run so it is not a problem.
 */
public class Day9DiskFragmenter {
    private record SolutionInput(List<Optional<Long>> physicalDisk, List<Pair<Long, List<Integer>>> files) {}

    private SolutionInput parse(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                Day9DiskFragmenter.class.getClassLoader().getResourceAsStream(filename))))) {
            final List<Optional<Long>> physicalDisk = new ArrayList<>();
            final List<Pair<Long, List<Integer>>> files = new ArrayList<>();

            final String line = reader.readLine();
            long ident = 0L;
            // blockId tracks the offsets in the physicalDisk
            int blockId = 0;

            for (int diskMapIdx = 0; diskMapIdx <= line.length()-1; diskMapIdx +=2) {
                int block_num = Integer.parseInt(line.substring(diskMapIdx, diskMapIdx+1));
                int freeNum = (diskMapIdx + 1 < line.length()) ? Integer.parseInt(line.substring(diskMapIdx + 1, diskMapIdx + 2)) : 0;

                var fileBlockIds = new ArrayList<Integer>();
                for (int i = 0; i < block_num; i++) {
                    physicalDisk.add(Optional.of(ident));
                    fileBlockIds.add(blockId);
                    blockId += 1;
                }
                files.add(new Pair<>(ident, fileBlockIds));

                for (int i = 0; i < freeNum; i++) { physicalDisk.add(Optional.empty()); blockId += 1; }

                ident += 1;
            }
            return new SolutionInput(physicalDisk, files);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static int findNextFree(List<Optional<Long>> physicalDisk, int startIdx) {
        for (int i = startIdx; i < physicalDisk.size(); i++) {
            if (physicalDisk.get(i).isEmpty()) { return i; }
        }
        return -1;
    }

    private static int findNextNonFreeFromEnd(List<Optional<Long>> physicalDisk, int startIdx) {
        for (int i = startIdx; i >= 0; i--) {
            if (physicalDisk.get(i).isPresent()) { return i; }
        }
        return -1;
    }

    private static void printDisk(List<Optional<Long>> physicalDisk) {
        for (var singleBlock : physicalDisk) {
            System.out.print(singleBlock.map(aLong -> String.format("%s", aLong)).orElse("."));
        }
        System.out.println();
    }

    /**
     * Find the first contiguous free space that can accommodate the file blocks. Search has to start from the beginning
     * of the physical disk because we are supposed to use the first free space that can accommodate the file blocks.
     *
     * @param physicalDisk The physical disk composed of blocks
     * @param size The size of the file to move in blocks
     * @param searchTillThisBlock The search for free space is limited to this block identifier/physical disk index because this
     *              is the first block identifier occupied by a file
     * @return  The index of the first contiguous free space that can accommodate the file blocks
     */
    private static int findFirstContiguousFreeSpace(List<Optional<Long>> physicalDisk, int size,
                                                    long searchTillThisBlock) {
        int count = 0;
        for (int phyBlockId = 0; phyBlockId < physicalDisk.size() && phyBlockId < searchTillThisBlock; phyBlockId++) {
            if (physicalDisk.get(phyBlockId).isEmpty()) {
                count += 1;
                if (count == size) { return phyBlockId - size + 1; }
            } else {
                count = 0;
            }
        }
        return -1;
    }

    /**
     * Move the file to the contiguous free space identified by writeIdx. The file is represented by the fileToMove list
     * which contains the block identifiers of the file blocks.
     *
     * @param physicalDisk The physical disk
     * @param fileToMove The list of block identifiers occupied by the file
     * @param fileId The file identifier
     * @param writeIdx The index of the first contiguous free space that can accommodate the file blocks
     */
    private void moveFile(List<Optional<Long>> physicalDisk, List<Integer> fileToMove, Long fileId, int writeIdx) {
        // i will be used to read the block identifiers that comprise the file, and also to determine the write
        // index in the physical disk
        for (int i = 0; i < fileToMove.size(); i++) {
            // Swap the contents of the read and write blocks
            physicalDisk.set(writeIdx + i, Optional.of(fileId));
            physicalDisk.set(fileToMove.get(i), Optional.empty());
        }
    }

    private static long calculateChecksum(List<Optional<Long>> physicalDisk) {
        return IntStream.range(0, physicalDisk.size())
                .filter(i -> physicalDisk.get(i).isPresent())
                .mapToLong(i -> i * physicalDisk.get(i).get())
                .sum();
    }

    public long part2(String fileName) {
        final var solutionInput = parse(fileName);
        final var physicalDisk = solutionInput.physicalDisk;
        final var files = solutionInput.files;

        for (int fileIdx = files.size() - 1; fileIdx >= 0; fileIdx--) {
            var fileToMove = files.get(fileIdx);
            int writeIdx = findFirstContiguousFreeSpace(physicalDisk, fileToMove.second().size(), fileToMove.second().getFirst());

            if (writeIdx != -1) {
                moveFile(physicalDisk, fileToMove.second(), fileToMove.first(), writeIdx);
            }
        }

        return calculateChecksum(physicalDisk);
    }

    public long part1(String fileName) {
        final var solutionInput = parse(fileName);
        final var physicalDisk = solutionInput.physicalDisk;

        int writeIdx = findNextFree(physicalDisk, 0);
        int readIdx = findNextNonFreeFromEnd(physicalDisk, physicalDisk.size()-1);

        while (writeIdx < readIdx) {
            Collections.swap(physicalDisk, readIdx, writeIdx);

            writeIdx = findNextFree(physicalDisk, writeIdx+1);
            readIdx = findNextNonFreeFromEnd(physicalDisk, readIdx-1);
        }

        return calculateChecksum(physicalDisk);
    }
}
