package lab11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamingGrep
{
    private static Stream<String> splitByWord(String string)
    {
        Stream.Builder<String> builder = Stream.builder();

        Pattern p = Pattern.compile("\\W");
        Stream.of(p.split(string)).filter(s -> s.length() > 0).forEach(builder::accept);

        return builder.build();
    }

    private static Stream<String> splitBySubWord(String string, int subWordLength)
    {
        Stream.Builder<String> builder = Stream.builder();
        IntStream.range(0, Math.min(string.length(), subWordLength)).forEach(i -> builder.accept(string.substring(i, i + subWordLength)));

        return builder.build();
    }

    private static long getCount2(URL url, String searchWord) throws IOException
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));

        return bf.lines().flatMap(s -> splitBySubWord(s, searchWord.length())).map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).count();
    }

    private static long grepCount(URL url, String searchWord) throws IOException
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));

       // Not legal
//       long count = 0;
//       bf.lines().flatMap(GrepProgression::splitByWord).map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).forEach(next -> count++);

        // Long way
//        return bf.lines().flatMap(GrepProgression::splitByWord).map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).
//            mapToLong(count -> 1).reduce(0, (l, r) -> l + r);

        // Using the built-in function count
        return bf.lines().flatMap(StreamingGrep::splitByWord).map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).count();
    }

    private static String grepByReduction(URL url, String searchWord) throws IOException
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));

        // Accumulate the string
//        bf.lines().map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).reduce("", (l, r) -> l.concat(r).concat(", "));

        // Parallel accumulation
        return bf.lines().parallel().map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).reduce("", (l, r) -> l.concat(r).concat(", "));
    }

    private static List<String> grepByCollection(URL url, String searchWord) throws IOException
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));

        // Use collect instead of reduce
//        return bf.lines().map(String::toUpperCase).
//            filter(s -> s.contains(searchWord.toUpperCase())).collect(ArrayList<String>::new, ArrayList<String>::add, ArrayList<String>::addAll);

        // Use collect instead of reduce
//        return bf.lines().map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).collect(Collectors.toList());

        // Implement my own collector
        List<String> list = bf.lines().map(String::toUpperCase).filter(s -> s.contains(searchWord.toUpperCase())).collect(new MyCollector());

        return list;
    }

    private static class MyCollector implements Collector<String, List<String>, List<String>>
    {
        private int lineCount = 0;

        @Override
        public Supplier<List<String>> supplier()
        {
            return ArrayList<String>::new;
        }

        @Override
        public BiConsumer<List<String>, String> accumulator()
        {
            return (list, nextString) -> list.add(getLineCount() + ": " +nextString);
        }

        @Override
        public BinaryOperator<List<String>> combiner()
        {
            return (list1, list2) -> {list1.addAll(list2); return list1;};
        }

        @Override
        public Function<List<String>, List<String>> finisher()
        {
            return list -> list;
        }

        @Override
        public Set<Characteristics> characteristics()
        {
            HashSet<Characteristics> characteristics = new HashSet<>();
            characteristics.add(Characteristics.CONCURRENT);

            return characteristics;
        }

        private synchronized int getLineCount()
        {
            return ++lineCount;
        }
    }

    public static void main(String... args)
    {
        try
        {
            if (args.length == 2)
            {
                URL url = new URL(args[0]);
                String searchWord = args[1];
                grepByCollection(url, searchWord).forEach(System.out::println);
            }
        }
        catch (IOException exception)
        {
            System.err.print("Invalid URL: " + exception.getMessage());
        }
    }
}
