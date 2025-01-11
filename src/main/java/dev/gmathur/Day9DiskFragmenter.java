package dev.gmathur;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;


/**
 * Solution for Day 9 of Advent of Code 2024 - Disk Fragmenter
 * Problem description: <a href="https://adventofcode.com/2024/day/9">...</a>
 * <p>
 * Part 1
 * 00...111...2...333.44.5555.6666.777.888899
 *
 */
public class Day9DiskFragmenter {
    private record SingleBlock(Long blockId, Optional<Long> fileId) {}
    private record SolutionInput(List<SingleBlock> physicalDisk, List<List<SingleBlock>> files) {}

    private SolutionInput parse(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                Day9DiskFragmenter.class.getClassLoader().getResourceAsStream(filename))))) {
            final List<SingleBlock> physicalDisk = new ArrayList<>();
            final List<List<SingleBlock>> files = new ArrayList<>();
            final List<List<SingleBlock>> freeSpaces = new ArrayList<>();

            final String line = reader.readLine();
            long ident = 0L;
            long blockId = 0L;

            for (int diskMapIdx = 0; diskMapIdx <= line.length()-1; diskMapIdx +=2) {
                int block_num = Integer.parseInt(line.substring(diskMapIdx, diskMapIdx+1));
                int freeNum = (diskMapIdx + 1 < line.length()) ? Integer.parseInt(line.substring(diskMapIdx + 1, diskMapIdx + 2)) : 0;

                List<SingleBlock> fileBlocks = new ArrayList<>();
                List<SingleBlock> freeBlocks = new ArrayList<>();

                for (int i = 0; i < block_num; i++) { fileBlocks.add(new SingleBlock(blockId++, Optional.of(ident))); }
                for (int i = 0; i < freeNum; i++) { freeBlocks.add(new SingleBlock(blockId++, Optional.empty())); }
                physicalDisk.addAll(fileBlocks);
                physicalDisk.addAll(freeBlocks);

                files.add(fileBlocks);

                ident += 1;
            }
            return new SolutionInput(physicalDisk, files);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static int findNextFree(List<SingleBlock> physicalDisk, int startIdx) {
        for (int i = startIdx; i < physicalDisk.size(); i++) {
            if (physicalDisk.get(i).fileId().isEmpty()) { return i; }
        }
        return -1;
    }

    private static int findNextNonFreeFromEnd(List<SingleBlock> physicalDisk, int startIdx) {
        for (int i = startIdx; i >= 0; i--) {
            if (physicalDisk.get(i).fileId().isPresent()) { return i; }
        }
        return -1;
    }

    private static void printDisk(List<SingleBlock> physicalDisk) {
        for (SingleBlock singleBlock : physicalDisk) {
            System.out.print(singleBlock.fileId().isEmpty() ? "." : String.format("%s", singleBlock.fileId().get()));
        }
        System.out.println();
    }

    private static int findFirstContiguousFreeSpace(List<SingleBlock> physicalDisk, int size, long limit) {
        int count = 0;
        for (int i = 0; i < physicalDisk.size() && physicalDisk.get(i).blockId < limit; i++) {
            if (physicalDisk.get(i).fileId().isEmpty()) {
                count += 1;
                if (count == size) { return i - size + 1; }
            } else {
                count = 0;
            }
        }
        return -1;
    }

    private void moveFile(List<SingleBlock> physicalDisk, List<SingleBlock> fileToMove, int writeIdx) {
        for (int i = 0; i < fileToMove.size(); i++) {
            SingleBlock w = physicalDisk.get(writeIdx + i);
            SingleBlock r = fileToMove.get(i);

            // Swap the contents of the read and write blocks
            SingleBlock newBlock = new SingleBlock(w.blockId(), r.fileId());
            physicalDisk.set(writeIdx + i, newBlock);

            newBlock = new SingleBlock(r.blockId(), Optional.empty());
            physicalDisk.set(r.blockId().intValue(), newBlock);
        }
    }

    private static long calculateChecksum(List<SingleBlock> physicalDisk) {
        return IntStream.range(0, physicalDisk.size())
                .filter(i -> physicalDisk.get(i).fileId().isPresent())
                .mapToLong(i -> i * physicalDisk.get(i).fileId().get())
                .sum();
    }

    public long part2(String fileName) {
        final var solutionInput = parse(fileName);
        final var physicalDisk = solutionInput.physicalDisk;
        final var files = solutionInput.files;

        int fileIdx = files.size()-1;
        List<SingleBlock> fileToMove = files.get(fileIdx);
        int writeIdx = findFirstContiguousFreeSpace(physicalDisk, fileToMove.size(), fileToMove.getFirst().blockId);

        while (true) {
            if (writeIdx != -1) {
                moveFile(physicalDisk, fileToMove, writeIdx);
            }

            fileIdx -= 1;
            if (fileIdx < 0) {
                break;
            }
            fileToMove = files.get(fileIdx);
            writeIdx = findFirstContiguousFreeSpace(physicalDisk, fileToMove.size(), fileToMove.getFirst().blockId);
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

        // Go through the physical disk and multiply the index of the block with the block fileId, if it is not empty
        return calculateChecksum(physicalDisk);
    }
}
