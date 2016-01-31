/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package demo.enumj;

import enumj.Enumerator;
import enumj.LateBindingEnumerator;
import enumj.ShareableEnumerator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Demonstrates how to use the Enumerator interface.
 *
 * @author Marius Filip
 */
public final class EnumeratorDemo {

    private static List<Integer> _123 = new ArrayList<>();

    public static void demo() {
        System.out.println("Demonstrating Enumerator methods ...");

        _123.add(1); _123.add(2); _123.add(3);
        final String pre = "  ";

        demoEnumerating(pre);
        demoOn(pre);
        demoOfArray(pre);
        demoOfIterator(pre);
        demoOfIterable(pre);
        demoOfStream(pre);
        demoOfSupplier(pre);
        demoOfLazyIterator(pre);
        demoOfLateBinding(pre);
        demoAsFiltered(pre);
        demoAsOptional(pre);
        demoAsShareable(pre);
        demoAsTolerant(pre);
        demoAllMatch(pre);
        demoAnyMatch(pre);
        demoAppend(pre);
        demoChoiceOf(pre);
        demoCollect(pre);
        demoConcat(pre);
        demoContains(pre);
        demoCount(pre);
        demoDistinct(pre);
        demoElementAt(pre);
        demoElementsEqual(pre);
        demoEmpty(pre);
        demoFilter(pre);
        demoFirst(pre);
        demoFlatMap(pre);
        demoIterate(pre);
        demoLast(pre);
        demoLimit(pre);
        demoLimitWhile(pre);
	demoMap(pre);
	demoMax(pre);
	demoMin(pre);
	demoNoneMatch(pre);
	demoPeek(pre);
	demoPrepend(pre);
	demoPrependOn(pre);
	demoRangeInt(pre);
	demoReduce(pre);
	demoRepeat(pre);
	demoRepeatAllSupplier(pre);
	demoRepeatAllElements(pre);
	demoRepeatEach(pre);
	demoReverse(pre);
	demoSingle(pre);
	demoZip(pre);
    }

    private static void demoEnumerating(String pre) {
        System.out.println(pre +
                           "New enumerator enumerating: " +
                           Enumerator.on(1, 2, 3).enumerating());
    }
    private static void demoOn(String pre) {
        final Enumerator<Integer> en = Enumerator.on(1, 2, 3);
        System.out.println(pre + "Elements in new enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoOfArray(String pre) {
        final Enumerator<Integer> en = Enumerator.of(
                _123.toArray(new Integer[0]));
        printLn(en, "Elements in enumerator from array:", pre);
    }
    private static void demoOfIterator(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123.iterator());
        printLn(en, "Elements in enumerator from iterator:", pre);
    }
    private static void demoOfIterable(String pre) {
        final Enumerator<Integer> en = Enumerator.of((Iterable<Integer>)_123);
        printLn(en, "Elements in enumerator from iterable:", pre);
    }
    private static void demoOfStream(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123.stream());
        printLn(en, "Elements in enumerator from stream:", pre);
    }
    private static void demoOfSupplier(String pre) {
        final MutableInt it = new MutableInt(1);
        final Supplier<Optional<Integer>> supl = () -> {
            final int val = it.getValue();
            if (val > 3) {
                return Optional.empty();
            }
            it.add(1);
            return Optional.of(val);
        };
        final Enumerator<Integer> en = Enumerator.of(supl);
        printLn(Enumerator.of(supl),
                "Elements in supplied enumerator:",
                pre);
    }
    private static void demoOfLazyIterator(String pre) {
        final Enumerator<Integer> en = Enumerator.of(() -> _123.iterator());
        printLn(en,
                "Elements in enumerator from iterator supplied lazily:",
                pre);
    }
    private static void demoOfLateBinding(String pre) {
        final LateBindingEnumerator<Integer> en =
                Enumerator.ofLateBinding(Integer.class);
        en.bind(_123.iterator());
        printLn(en, "Elements in enumerator bound late:", pre);
    }
    private static void demoAsFiltered(String pre) {
        printLn(Enumerator.of(_123).asFiltered(Long.class),
                "Number of elements compatible with Long: ",
                pre);
    }
    private static void demoAsOptional(String pre) {
        printLn(Enumerator.of(_123).asOptional().take(5),
                "Optional elements in enumerator:",
                pre);
        System.out.println(pre + "...");
    }
    private static void demoAsShareable(String pre) {
        final ShareableEnumerator<Integer> en =
                Enumerator.of(_123).asShareable();
        final Enumerator<Integer> en1 = en.share();
        final Enumerator<Integer> en2 = en.share();
        System.out.println(pre + "Shared enumerated elements:");
        while(en1.hasNext() || en2.hasNext()) {
            final int val1_1 = en1.hasNext() ? en1.next() : 0;
            final int val1_2 = en1.hasNext() ? en1.next() : 0;
            final int val2 = en2.next();
            System.out.println(
                    pre + pre +
                    "val 1 (first)  = " + (val1_1>0?val1_1+" ":"none ") +
                    "val 2 (first)  = " + (val1_2>0?val1_2+" ":"none ") +
                    "val 3 (second) = " + val2);
        }
    }
    private static void demoAsTolerant(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                .map(i -> {
                  if (i == 2) {
                      throw new UnsupportedOperationException("2 not allowed!");
                  }
                  return i;
                })
                .asTolerant(e -> System.err.println("Error: " + e));
        printLn(en, "Elements of fault-tolerant enumerator: ", pre);
    }
    private static void demoAllMatch(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Whether all elements are >=2: " +
                                 en.allMatch(i -> i>=2));
    }
    private static void demoAnyMatch(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Whether some elements are >=2: " +
                                 en.anyMatch(i -> i>=2));
    }
    private static void demoAppend(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123).append(4, 5);
        System.out.println(pre + "Elements of appended enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoChoiceOf(String pre) {
        final Random rnd = new Random();
        final Enumerator<Integer> en = Enumerator.choiceOf(
                () -> rnd.nextInt(2),
                _123.iterator(),
                Enumerator.on(4, 5, 6, 7, 8));
        printLn(en, "Randomly chosen elements:", pre);
    }
    private static void demoCollect(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        final Set<Integer> s = en.collect(Collectors.toSet());
        System.out.println(pre + "Elements of collected set:");
        for(Integer i : s) {
            System.out.println(pre + pre + i);
        }
    }
    private static void demoConcat(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                .concat(Enumerator.on(4, 5, 6));
        printLn(en, "Elements of concatenated enumerator:", pre);
    }
    private static void demoContains(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "List " + _123 + " contains 1: " +
                                 en.contains(1));
    }
    private static void demoCount(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Number of elements in enumerator: " +
                                 en.count());
    }
    private static void demoDistinct(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                .concatOn(2, 3, 4)
                .distinct();
        printLn(en, "Distinct elements of enumerator:", pre);
    }
    private static void demoElementAt(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Enumerated element at 2: " +
                                 en.elementAt(2));
    }
    private static void demoElementsEqual(String pre) {
        final Enumerator<Integer> en1 = Enumerator.of(_123);
        final Enumerator<Integer> en2 = Enumerator.of(_123).reverse();
        System.out.println(pre + "Elements are same in reverse: " +
                                 en1.elementsEqual(en2));
    }
    private static void demoEmpty(String pre) {
        System.out.println(pre + "Number of elements in empty enumerator: " +
                                 Enumerator.empty().count());
    }
    private static void demoFilter(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                .filter(i -> 0 == i%2);
        printLn(en, "Even elements of enumerator: ", pre);
    }
    private static void demoFirst(String pre) {
        System.out.println(pre + "First element of empty enumerator: " +
                                 Enumerator.empty().first());
    }
    private static void demoFlatMap(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                .flatMap(i -> Enumerator.on(i, i));
        printLn(en, "Double elements of enumerator:", pre);
    }
    private static void demoIterate(String pre) {
        final Enumerator<Double> en = Enumerator
                .iterate(0.0, i -> Math.sqrt(i*i+1))
                .limit(16);
        System.out.println(pre + "Iterated elements:");
        System.out.print(pre + pre);
        en.forEach(i -> System.out.print(" " + i));
        System.out.println();
    }
    private static void demoLast(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Last element: " + en.last());
    }
    private static void demoLimit(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123).limit(2);
        printLn(en, "Elements of limited enumerator:", pre);
    }
    private static void demoLimitWhile(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                                                 .limitWhile(i -> i<2);
        printLn(en, "Elements of enumerator limited by cond:", pre);
    }
    private static void demoMap(String pre) {
        final Enumerator<Pair<Long, Integer>> en = Enumerator.of(_123)
                .map(x -> x*x)
                .map((x, i) -> Pair.of(i, x));
        printLn(en, "Squared elements of enumerator:", pre);
    }
    private static void demoMax(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Max. of enumerator elements: " +
                           en.max(Integer::compare));
    }
    private static void demoMin(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Min. of enumerator elements: " +
                           en.max(Integer::compare));
    }
    private static void demoNoneMatch(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "5 doesn't match: " +
                           en.noneMatch(i -> i == 5));
    }
    private static void demoPeek(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        printLn(en.peek(i -> System.out.println(i)),
                "Peeking enumeratored elements:",
                pre);
    }
    private static void demoPrepend(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        final Integer[] prefix = {-1, -2, -3};
        printLn(en.prepend(Enumerator.of(prefix)),
                "Prepended elements: ",
                pre);
    }
    private static void demoPrependOn(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        printLn(en.prependOn(-1, -2, -3),
                "Prepended elements: ",
                pre);
    }
    private static void demoRangeInt(String pre) {
        System.out.println(pre + "Range 0 .. 9:");
        printLn(Enumerator.rangeInt(0, 10),
                "Range 0 .. 9:",
                pre);
    }
    private static void demoReduce(String pre) {
        System.out.println(pre + "Sum 0 .. 9: " +
                           Enumerator.rangeInt(0, 10)
                                     .reduce((x,y) -> x+y));
    }
    private static void demoRepeat(String pre) {
        printLn(Enumerator.repeat(1969, 5),
                "Repeating the same element:",
                pre);
    }
    private static void demoRepeatAllSupplier(String pre) {
        printLn(Enumerator.repeatAll(() -> Enumerator.rangeInt(0, 3), 3),
                "Repeating same enumerator:",
                pre);
    }
    private static void demoRepeatAllElements(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        printLn(en.repeatAll(3), "Repeating enumerator:", pre);
    }
    private static void demoRepeatEach(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        printLn(en.repeatEach(3),
                "Repeating each enumerated element:",
                pre);
    }
    private static void demoReverse(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        printLn(en.reverse(), "Reversed elements:", pre);
    }
    private static void demoSingle(String pre) {
        System.out.println(pre + "Single element: " +
                           Enumerator.on(5).single());
    }
    private static void demoZip(String pre) {
        for(int i=0; i<=4; ++i) {
            final Enumerator<Integer> _123 = Enumerator.on(1, 2, 3);
            final Enumerator<Integer> _45678 = Enumerator.on(4, 5, 6, 7, 8);
            switch(i) {
                case 1:
                    printLn(_123.zipAny(_45678),
                            "Zipped elements (any):",
                            pre);
                    break;
                case 2:
                    printLn(_123.zipBoth(_45678),
                            "Zipped elements (both):",
                            pre);
                    break;
                case 3:
                    printLn(_123.zipLeft(_45678),
                            "Zipped elements (left):",
                            pre);
                    break;
                case 4:
                    printLn(_123.zipRight(_45678),
                            "Zipped elements (right):",
                            pre);
                    break;
            }
        }
    }

    private static <T> void printLn(Enumerator<T> en, String msg, String pre) {
        System.out.println(pre + msg);
        en.forEach(e -> System.out.println(pre + pre + e));
    }
}
