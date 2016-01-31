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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.mutable.MutableInt;

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
        System.out.println(pre + "Elements in enumerator from array:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoOfIterator(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123.iterator());
        System.out.println(pre + "Elements in enumerator from iterator:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoOfIterable(String pre) {
        final Enumerator<Integer> en = Enumerator.of((Iterable<Integer>)_123);
        System.out.println(pre + "Elements in enumerator from iterable:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoOfStream(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123.stream());
        System.out.println(pre + "Elements in enumerator from stream:");
        en.forEach(i -> System.out.println(pre + pre + i));
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
        System.out.println(pre + "Elements in supplied enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoOfLazyIterator(String pre) {
        final Enumerator<Integer> en = Enumerator.of(() -> _123.iterator());
        System.out.println(pre + "Elements in enumerator from iterator " +
                                 "supplied lazily:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoOfLateBinding(String pre) {
        final LateBindingEnumerator<Integer> en =
                Enumerator.ofLateBinding(Integer.class);
        en.bind(_123.iterator());
        System.out.println(pre + "Elements in enumerator bound late:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoAsFiltered(String pre) {
        final Enumerator<Long> en = Enumerator.of(_123).asFiltered(Long.class);
        System.out.println(pre + "Number of elements compatible with Long: " +
                                 en.count());
    }
    private static void demoAsOptional(String pre) {
        final Enumerator<Optional<Integer>> en =
                Enumerator.of(_123).asOptional();
        System.out.println(pre + "Optional elements in enumerator:");
        en.take(5).forEach(i -> System.out.println(pre + pre + i));
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
        System.out.println(pre + "Elements of fault-tolerant enumerator: ");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoAllMatch(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Wherther all elements are >=2: " +
                                 en.allMatch(i -> i>=2));
    }
    private static void demoAnyMatch(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123);
        System.out.println(pre + "Wherther some elements are >=2: " +
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
        System.out.println(pre + "Randomly chosen elements:");
        en.forEach(i -> System.out.println(pre + pre + i));
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
        System.out.println(pre + "Elements of concatenated enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));
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
        System.out.println(pre + "Distinct elements of enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));        
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
        System.out.println(pre + "Even elements of enumerator: ");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoFirst(String pre) {
        System.out.println(pre + "First element of empty enumerator: " +
                                 Enumerator.empty().first());
    }
    private static void demoFlatMap(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123)
                .flatMap(i -> Enumerator.on(i, i));
        System.out.println(pre + "Double elements of enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));
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
        System.out.println(pre + "Elements of limited enumerator:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
    private static void demoLimitWhile(String pre) {
        final Enumerator<Integer> en = Enumerator.of(_123).limitWhile(i -> i<2);
        System.out.println(pre + "Elements of enumerator limited by cond:");
        en.forEach(i -> System.out.println(pre + pre + i));
    }
}
