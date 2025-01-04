package dev.gmathur;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Day9DiskFragmenter {
    private record Block(Optional<Long> id) {}
    private record SolutionInput(List<Block> physicalDisk) {
    }

    private SolutionInput parse(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                Day9DiskFragmenter.class.getClassLoader().getResourceAsStream(filename))))) {
            final List<Block> physicalDisk = new ArrayList<>();
            final String line = reader.readLine();
            long ident = 0L;

            for (int diskMapIdx = 0; diskMapIdx <= line.length()-1; diskMapIdx +=2) {
                var block_num = Long.parseLong(line.substring(diskMapIdx, diskMapIdx+1));
                long freeNum = (diskMapIdx + 1 < line.length()) ? Long.parseLong(line.substring(diskMapIdx + 1, diskMapIdx + 2)) : 0;
                physicalDisk.addAll(Collections.nCopies((int) block_num, new Block(Optional.of(ident))));
                physicalDisk.addAll(Collections.nCopies((int) freeNum, new Block(Optional.empty())));
                ident += 1;
            }
            return new SolutionInput(physicalDisk);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static int findNextFree(List<Block> physicalDisk, int startIdx) {
        for (int i = startIdx; i < physicalDisk.size(); i++) {
            if (physicalDisk.get(i).id().isEmpty()) { return i; }
        }
        return -1;
    }

    private static int findNextNonFreeFromEnd(List<Block> physicalDisk, int startIdx) {
        for (int i = startIdx; i >= 0; i--) {
            if (physicalDisk.get(i).id().isPresent()) { return i; }
        }
        return -1;
    }

    private static void printDisk(List<Block> physicalDisk) {
        for (Block block : physicalDisk) {
            System.out.print(block.id().isEmpty() ? "." : block.id().get());
        }
        System.out.println();
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

        // Go through the physical disk and multiply the index of the block with the block id, if it is not empty
        return IntStream.range(0, physicalDisk.size())
                .filter(i -> physicalDisk.get(i).id().isPresent())
                .mapToLong(i -> i * physicalDisk.get(i).id().get())
                .sum();
    }

}
