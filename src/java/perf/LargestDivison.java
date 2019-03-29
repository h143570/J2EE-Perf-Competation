package perf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LargestDivison {

    private static final int                   CACHE_SIZE                           = 99950;
    private static final int                   MAX_NUMBER_OF_PRE_GEN_CACHE_ELEMENTS = 94235;

    private static long[]                      preGenCache;
    private static ConcurrentMap<String, Long> cache                                = new ConcurrentHashMap<String, Long>(CACHE_SIZE + 50, 1, 100);
    private static long                        maxElement                           = 0;
    private static long                        increment                            = 1;

    private static final Long                  MINUS_ONE                            = -1L;

    private static long[]                      primeTillIncrement;
    private static final long[]                primes                               = new long[]{2, 3, 5, 7, 11, 13};

    public LargestDivison() {
        super();

        if (preGenCache == null) {

            /**
             * Initializes the class to use with (X*N + I) where I < X algorithm, where X is the product of primes
             */

            //calculate the value of X
            for (long prim : primes) {
                increment *= prim;
            }

            //calculate the possible values of I
            List<Long> primesTillIncrementList = new ArrayList<>();
            cont: for (long l = 1; l < increment; l++) {
                for (long prime : primes) {
                    if (l % prime == 0) {
                        continue cont;
                    }
                }
                primesTillIncrementList.add(l);
            }
            System.out.println(increment);
            System.out.println(primesTillIncrementList.size());
            System.out.println((double) increment / (double) primesTillIncrementList.size());

            //convert List<Long> to long[]
            primeTillIncrement = new long[primesTillIncrementList.size()];
            for (int i = 0; i < primesTillIncrementList.size(); i++) {
                primeTillIncrement[i] = primesTillIncrementList.get(i);
            }

            preGenCache = new long[MAX_NUMBER_OF_PRE_GEN_CACHE_ELEMENTS];

            int i = 0;
            long numb = primes[primes.length - 1] + 2;

            //populate cache with primes excluding the initial ones
            while (i < MAX_NUMBER_OF_PRE_GEN_CACHE_ELEMENTS) {

                if (doWorkForPreGenCache(numb, (long) Math.sqrt(numb)) == 0) {
                    if (i % 10000 == 0) {
                        System.out.println(i);
                    }
                    preGenCache[i] = numb;
                    i++;
                }
                numb += 2;

            }
            long maxElement = preGenCache[MAX_NUMBER_OF_PRE_GEN_CACHE_ELEMENTS - 1];
            System.out.println(maxElement);
            /**
             * Determine the starting element (increment * N), based on the highest element in the pregen cache.
             * Taking advantage of rounding in case of non floating point numbers.
             *
             */
            maxElement = (maxElement / increment) * increment;

        }
    }

    /**
     *
     * Get largest real division of the given number. If number is a prime, it gives back 1. If number is less than 2, or it cannot be parsed to long,
     * method returns -1.
     *
     * @param number to get the real largest divisions.
     * @return the real largest divisions, 1 if number is a prime. -1 in case of error.
     */
    public long getLargestDivison(String number) {
        Long result = cache.get(number);
        if (result == null) {
            long tmpLong = 0;
            try {
                tmpLong = Long.parseLong(number);
            } catch (NumberFormatException nfe) {
            }
            if (tmpLong < 2) {
                return MINUS_ONE;
            }

            for (long prim : primes) {
                if (tmpLong % prim == 0) {
                    return tmpLong / prim;
                }
            }

            long tmpResult = doWorkInternallyFast(tmpLong);
            if (tmpResult == 0L) {
                tmpResult = doWorkInternallyRemainder4(tmpLong, (long) Math.sqrt(tmpLong));
            }
            result = tmpResult;

            storeInCache(number, result);
        }

        return result;
    }

    /**
     * Give back your name.
     * @return your name.
     */
    public static String getYourName() {
        return "Bessenyei Balazs";
    }

    private void storeInCache(String number, Long result) {
        if (cache.size() >= CACHE_SIZE) {
            Iterator<Entry<String, Long>> it = cache.entrySet().iterator();
            it.next();
            it.remove();
        }
        cache.put(number, result);
    }

    private static final long doWorkForPreGenCache(final long n, final long sqrt) {
        for (long i = 3; i <= sqrt; i += 2) {
            if (n % i == 0) {
                return 1;
            }
        }
        return 0;
    }

    private static final long doWorkInternallyFast(final long n) {
        for (long j : preGenCache) {
            if (n % j == 0) {
                return n / j;
            }
        }
        return 0;
    }

    private static final long doWorkInternallyRemainder4(final long n, final long sqrt) {
        for (long i = maxElement; i <= sqrt; i += increment) {

            long m;
            for (long l : primeTillIncrement) {
                m = i + l;
                if (m == 1) {
                    continue;
                }
                if ((n % m == 0)) {
                    return n / m;
                }
            }
        }
        return 1;
    }

//        private static final long doWorkInternallyRemainder5(final long n, final long sqrt) {
//            for (long i = 0; i <= sqrt; i += increment) {
//
//                long m;
//                for (long l : primeTillIncrement) {
//                    m = i + l;
//                    if (m == 1) {
//                        continue;
//                    }
//                    if ((n % m == 0)) {
//                        return n / m;
//                    }
//                }
//            }
//            return 1;
//        }
}
