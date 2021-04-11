package com.home.samples.strings;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(jvmArgsAppend = {"-Xms2g", "-Xmx2g"})
public class StringConcatenationBenchmark {
    private static final String ENGLISH_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String RUSSIAN_ALPHABET = "абвгдеёжзиклмнопрстуфхцчшщьыъэюя";

    @State(Scope.Thread)
    public static class Data {
        /* @Param({"true", "false"})
         private boolean latin;*/
        @Param({"10"})
//        @Param({"10", "100", "1000"})
        private int stringsCount;

//        @Param({"1", "10", "100"})
        @Param({"10"})
        private int stringLength;

        private String[] stringsArray;

        @Setup
        public void setup() {
//            String alphabet = latin ? ENGLISH_ALPHABET : RUSSIAN_ALPHABET;
            RandomStringGenerator generator = new RandomStringGenerator();
            stringsArray = new String[stringsCount];
            for (int i = 0; i < stringsCount; i++) {
                String string = generator.randomString(ENGLISH_ALPHABET, stringLength);
                stringsArray[i] = string;
            }
        }
    }

//    @Benchmark
    public String stringConcat_ConcatStringBuilder(Data data) {
        StringBuilder stringBuilder = new StringBuilder(data.stringsArray.length * data.stringLength);
        for (String s : data.stringsArray) {
            stringBuilder.append(s).append(", ");
        }
        return stringBuilder.toString();
    }

//    @Benchmark
    public String stringConcat_ConcatStringBuffer(Data data) {
        StringBuffer stringBuffer = new StringBuffer(data.stringsArray.length * data.stringLength);
        for (String s : data.stringsArray) {
            stringBuffer.append(", ").append(s);
        }
        return stringBuffer.toString();
    }

//    @Benchmark
    public String stringConcat_ConcatStringPlus(Data data) {
        String result = "";
        for (String s : data.stringsArray) {
            result += s + ", ";
        }
        return result;
    }

//    @Benchmark
    public String stringConcat_ConcatStringJoiner(Data data) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String s : data.stringsArray) {
            stringJoiner.add(s);
        }
        return stringJoiner.toString();
    }

    @Benchmark
    public String stringConcat_ConcatStringJoin(Data data) {
        return String.join(", ", data.stringsArray);
    }

    @Benchmark
    public String stringConcat_ConcatStringConcat(Data data) {
        String result = "";
        for (String s : data.stringsArray) {
            result = result.concat(s).concat(", ");
        }
        return result;
    }

    @Benchmark
    public String stringConcat_ConcatStreamCollectorsJoining(Data data) {
        return Arrays.stream(data.stringsArray).collect(Collectors.joining(", "));
    }
}
