package de.featjar.base.data;

import de.featjar.base.Feat;
import de.featjar.base.computation.Computable;
import de.featjar.base.computation.FutureResult;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;


class ComputableTest {
    @Test
    void simpleComputation() {
        {
            Computable<Integer> computable = Computable.of(42);
            assertEquals(42, computable.getResult().get());
            assertFalse(Feat.cache().has(computable));
        }
        assertTrue(Feat.cache().computationMap.isEmpty());

        Feat.run(fj -> {
            Computable<Integer> computable = Computable.of(42);
            assertEquals(42, computable.getResult().get());
            assertTrue(Feat.cache().has(computable));
            assertFalse(Feat.cache().computationMap.isEmpty());
        });

        assertTrue(Feat.cache().computationMap.isEmpty());
        {
            Computable<Integer> computable = Computable.of(42);
            assertEquals(42, computable.getResult().get());
            assertFalse(Feat.cache().has(computable));
        }
        assertTrue(Feat.cache().computationMap.isEmpty());
    }

    static class ComputeIsEven implements Computable<Boolean> {
        Computable<Integer> input;

        public ComputeIsEven(Computable<Integer> input) {
            this.input = input;
        }

        @Override
        public FutureResult<Boolean> compute() {
            return input.get().thenCompute((integer, monitor) -> integer % 2 == 0);
        }
    }

    @Test
    void chainedComputation() {
        Computable<Integer> computable = Computable.of(42);
        Computable<Boolean> isEvenComputable = () -> computable.get().thenCompute((integer, monitor) -> integer % 2 == 0);
        assertTrue(isEvenComputable.getResult().get());
        assertTrue(computable.map(ComputeIsEven::new).getResult().get());
        assertTrue(computable.map(ComputeIsEven::new).getResult().get());
    }

    static class ComputeIsParity implements Computable<Boolean> {
        enum Parity { EVEN, ODD }
        Computable<Integer> input;
        Parity parity;

        public ComputeIsParity(Computable<Integer> input, Parity parity) {
            this.input = input;
            this.parity = parity;
        }

        @Override
        public FutureResult<Boolean> compute() {
            return input.get().thenCompute(
                    (integer, monitor) -> parity == Parity.EVEN ? integer % 2 == 0 : integer % 2 == 1);
        }
    }

    @Test
    void computationWithArguments() {
        Computable<Integer> computable = Computable.of(42);
        assertTrue(new ComputeIsParity(computable, ComputeIsParity.Parity.EVEN).getResult().get());
        assertFalse(new ComputeIsParity(computable, ComputeIsParity.Parity.ODD).getResult().get());
        assertTrue(computable.map(c -> new ComputeIsParity(c, ComputeIsParity.Parity.EVEN)).getResult().get());
    }

    @Test
    void allOfSimple() {
        Pair<Integer, Integer> r = Computable.of(Computable.of(1), Computable.of(2)).getResult().get();
        assertEquals(1, r.getKey());
        assertEquals(2, r.getValue());
    }

    @Test
    void allOfComplex() {
        Computable<Integer> c1 = Computable.of(42);
        Computable<Boolean> c2 = c1.map(ComputeIsEven::new);
        Pair<Integer, Boolean> r = Computable.of(c1, c2).getResult().get();
        assertEquals(42, r.getKey());
        assertEquals(true, r.getValue());
    }

    @Test
    void allOfSleep() {
        Computable<Integer> c1 = () -> FutureResult.wrap(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 42;
        }));
        Computable<Boolean> c2 = c1.map(ComputeIsEven::new);
        Pair<Integer, Boolean> r = Computable.of(c1, c2).getResult().get();
        assertEquals(42, r.getKey());
        assertEquals(true, r.getValue());
    }
}