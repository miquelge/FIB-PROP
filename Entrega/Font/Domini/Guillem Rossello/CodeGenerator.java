package model;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.abs;

public class CodeGenerator {

    /** Atributes */
    private singletonBoard board;
    private Set<Code> populationSet;
    private Set<Pair<Code, Pair<Integer,Integer> > > guesses;
    private Integer turn;

    /** Constructor */
    public CodeGenerator() {
        board = singletonBoard.getInstance();
        populationSet = new HashSet<>();
        guesses = new HashSet<>();
        turn = 0;
    }

    /** Getters & Setters */
    public Integer getLength() {return board.getLength();}

    public boolean getRepetitions() {return board.isRep();}

    public Integer getNumColors() {return board.getNum_colors();}

    public Set<Code> getPopulationSet() {
        return populationSet;
    }

    public Set<Pair<Code, Pair<Integer,Integer> > > getGuesses() {return guesses;}

    public void setPopulationSet(Set<Code> populationSet) {
        this.populationSet = populationSet;
    }

    public void newGuessesEntry(Pair<Code, Pair<Integer,Integer> >  guess) {guesses.add(guess);}

    /** Generates a random code according to the specified difficulty parameters
     * @Pre: Player is defined as the CodeBreaker
     * @Post: Returns the Objective Code generated by the machine for the current game */
    public Code m_generateCodeObjective() {
        Code code;
        if (board.isRep()) code = codi_random();
        else code = codi_random_no_rep();
        return code;
    }

    /** Generates a test code following a version of the Genetic ALgorithm
     * @Pre: Player is defined as the CodeMaker, and has already set an Objective Code. singleton instance of Board has already been initialized
     * @Post: Returns the code chosen by the machine to be tested against the Objective Code */
    public Code m_generateCodeTest(Code lastGuess, Pair<Integer,Integer> lastFB, Integer maxSons, Integer maxGen) {

        ++turn;

        if (turn.equals(1)) {
        	// easy
            if (board.getLength() == 4 && !board.isRep()) return new Code("0123");
        	// medium
			else if (board.getLength() == 4 && board.isRep()) return new Code("0011");
        	// hard
            else if (board.getLength() == 5 && board.isRep()) return new Code("00011");
			else return new Code ("00011");
        }

        /** We create an initial random population with size = 150 */
        populationSet = new HashSet<>();
        while (populationSet.size() < 150) {
            Code code;
            if (board.isRep()) code = codi_random();
            else code = codi_random_no_rep();
            populationSet.add(code);
        }

        /** We add the last guess and its feedback to the list of guesses */
        Pair<Code,Pair<Integer,Integer> > lastTurn = new Pair<>(lastGuess,lastFB);
        if (lastTurn.getKey().getCode() != "notWorking")
			guesses.add(lastTurn);

        Set<Code> chosenSons = new HashSet<>();
        Random random = new Random();
        Code firstMem, secondMem;
        Integer numGen = 0;

        /** We limit the number of sons and generations in order to optimize computational time */
        while (chosenSons.size() <= maxSons && numGen <= maxGen) {

            Set<Code> sons = new HashSet<>();
            Iterator it = populationSet.iterator();

            while (it.hasNext()) {
                firstMem = (Code) it.next();

                /** Condition to consider when we have only one code left to be treated */
                if (it.hasNext()) secondMem = (Code) it.next();
                else secondMem = firstMem;

                ArrayList<Code> newSons = new ArrayList<>();

                /** We apply the changes on the code, if the result has already been generated we add a random code instead */
                if (random.nextInt(100) < 50) newSons = crossover_1(firstMem, secondMem);
                else newSons = crossover_2(firstMem, secondMem);

                if (random.nextInt(100) < 3) {
                    newSons.set(0, mutate(newSons.get(0)));
                    newSons.set(1, mutate(newSons.get(1)));
                }
                if (random.nextInt(100) < 3) {
                    newSons.set(0, permutate(newSons.get(0)));
                    newSons.set(1, permutate(newSons.get(1)));
                }

                if (random.nextInt(100) < 2) {
                    newSons.set(0, inverse(newSons.get(0)));
                    newSons.set(1, inverse(newSons.get(1)));
                }

                while (!sons.add(newSons.get(0))) {
                    Code code;
                    if (board.isRep()) code = codi_random();
                    else code = codi_random_no_rep();
                    newSons.set(0, code);
                }

                while (!sons.add(newSons.get(1))) {
                    Code code;
                    if (board.isRep()) code = codi_random();
                    else code = codi_random_no_rep();
                    newSons.set(1, code);
                }

            }

            it = sons.iterator();
            Code son;
            Integer fitness;

            /** We create a sorted vector of pairs, each with a transformed code and its fitness value */
            ArrayList<Pair<Code, Integer>> sonwfit = new ArrayList<>();
            while (it.hasNext()) {
                son = (Code) it.next();
                fitness = fitnessValue(son, guesses);
                Pair<Code, Integer> fitnessPair = new Pair<>(son, fitness);
                sonwfit.add(fitnessPair);
            }
            sonwfit.sort(fitnessComparator);
            Set<Code> candidates = new HashSet<>();
            /** We check fitness value is correct and guesses doesn't contain the code. If we get to the first elm with a
             * fitness value != 0, we know we're done searching so we finish iterating*/
            for (int i = 0; i < sonwfit.size(); ++i) {
                Pair<Code, Integer> nextElm = sonwfit.get(i);
                if (nextElm.getValue().equals(0) && !guesses.contains(nextElm)) {
                    candidates.add(nextElm.getKey());
                }
                else break;
            }
            if (candidates.isEmpty()) ++numGen;

            /** We create a new population filled with the candidates and random codes up to 150*/
            Set<Code> new_population = new HashSet<>();
            new_population.addAll(candidates);
            while (new_population.size() < 150) {
                Code code;
                if (board.isRep()) code = codi_random();
                else code = codi_random_no_rep();
                new_population.add(code);
            }

            populationSet = new_population;
            /** We add to chosenSons the candidates of this iteration, only if they are not already there*/
            Iterator cIt = candidates.iterator();
            while(cIt.hasNext()) chosenSons.add((Code) cIt.next());

            ++numGen;
        }

        /** We select from the chosen codes the first one that is not already included in guesses */
        Code chosenSon = new Code("notWorking");
        Iterator chosenIt = chosenSons.iterator();
        while (chosenIt.hasNext()) {
            chosenSon = (Code) chosenIt.next();
            if (!guesses.contains(chosenSon)) break;
        }

        return chosenSon;
    }

    public Code codi_random_no_rep() {
        Code code = codi_random();
        ArrayList<Integer> rep = hasRepetitions(code);
        if (rep.size() != 0) {
            Set<Integer> colors = new HashSet<>();
            for (int k = 0; k < board.getLength(); ++k) {
                Integer elm = Character.getNumericValue(code.getCode().charAt(k));
                colors.add(elm);
            }
            code.changeRepeatedElms(rep, colors);
        }
        return code;
    }

    /** Comparator used to sort sonwfit vector */
    public static Comparator< Pair<Code,Integer> > fitnessComparator = new Comparator<Pair<Code,Integer>>() {

        @Override
        public int compare(Pair<Code, Integer> p1, Pair<Code, Integer> p2) {
            Integer f1 = p1.getValue().intValue();
            Integer f2 = p2.getValue().intValue();

            return f1.compareTo(f2);
        }
    };

    /** Generates a fitness value comparing the test code we want to know the fitness of with all the guesses
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns the fitnessValue of test */
    public Integer fitnessValue(Code test, Set<Pair<Code, Pair<Integer,Integer> > > guesses) {
        Iterator it = guesses.iterator();
        ArrayList<Pair<Integer, Integer>> differences = new ArrayList<>();
        while (it.hasNext()) {
            differences.add(getDifference(test, (Pair<Code, Pair<Integer, Integer>>) it.next()));
        }

        Integer blackdiff = 0;
        Integer whitediff = 0;
        for (int i = 0; i < differences.size(); ++i) {
            blackdiff += differences.get(i).getKey().intValue();
            whitediff += differences.get(i).getValue().intValue();
        }
        return blackdiff + whitediff;
    }

    /** Auxiliar function of fitnessValue, computes the difference between the reference feedback and the feedback
     * of test, considering the reference code as the objective code
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns the difference between test feedback and reference feedback */
    public Pair<Integer,Integer> getDifference(Code test,  Pair<Code, Pair<Integer,Integer>> reference) {

        Pair<Integer,Integer> testResult = test.getBN(reference.getKey());
        Integer N = abs(testResult.getKey().intValue() - reference.getValue().getKey().intValue());
        Integer B = abs(testResult.getValue().intValue() - reference.getValue().getValue().intValue());
        return new Pair<>(N,B);
    }

    /** Returns a Pair of codes which are the result of having a one point crossover between the two parameters
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns the crossover(1) between c1 and c2 */
    public ArrayList<Code> crossover_1(Code c1, Code c2) {
        Random random = new Random();
        int i = random.nextInt(board.getLength());
        String nou_1 = c1.getCode().substring(0, i) + c2.getCode().substring(i);
        Code code1 = new Code(nou_1);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(code1);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) code1.getCode().charAt(k));
                }
                code1.changeRepeatedElms(rep, colors);
            }
        }
        String nou_2 = c2.getCode().substring(0, i) + c1.getCode().substring(i);
        Code code2 = new Code(nou_2);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(code2);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) code2.getCode().charAt(k));
                }
                code2.changeRepeatedElms(rep, colors);
            }
        }
        return new ArrayList<>(Arrays.asList(code1, code2));
    }

    /** Returns a Pair of codes which are the result of having a two point crossover between the two parameters
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Return the crossover(2) between c1 and c2 */
    public ArrayList<Code> crossover_2(Code c1, Code c2) {
        Random random = new Random();
        int i = random.nextInt(board.getLength());
        int j = random.nextInt(board.getLength());
        if (j<i) {
            int tmp = j;
            j = i;
            i = tmp;
        }
        String nou_1 = c1.getCode().substring(0, i) + c2.getCode().substring(i, j) + c1.getCode().substring(j);
        Code code1 = new Code(nou_1);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(code1);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) code1.getCode().charAt(k));
                }
                code1.changeRepeatedElms(rep, colors);
            }
        }
        String nou_2 = c2.getCode().substring(0, i) + c1.getCode().substring(i, j) + c2.getCode().substring(j);
        Code code2 = new Code(nou_2);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(code2);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) code2.getCode().charAt(k));
                }
                code2.changeRepeatedElms(rep, colors);
            }
        }
        return new ArrayList<>(Arrays.asList(code1, code2));
    }

    /** Returns a random code
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns a random code*/
    public Code codi_random() {
        String nou = "";
        Random random = new Random();
        for (int i = 0; i < board.getLength(); i++) nou = nou + random.nextInt(board.getNum_colors());
        return new Code(nou);
    }

    /** Returns a mutation of the code recieved as a parameter
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Return a mutated code based on c */
    public Code mutate(Code c) {

        Random random = new Random();
        int i = random.nextInt(board.getLength());
        int j = random.nextInt(board.getNum_colors()-1);
        String nou = c.getCode().substring(0, i) + String.valueOf(j) + c.getCode().substring(i+1);
        Code nouCodi = new Code(nou);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(nouCodi);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) nouCodi.getCode().charAt(k));
                }
                nouCodi.changeRepeatedElms(rep, colors);
            }
        }
        return nouCodi;
    }

    /** Returns a permutation of the code recieved as a parameter
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns a permutated code based on c */
    public Code permutate(Code c) {
        Random random = new Random();
        int i = random.nextInt(board.getLength());
        int j = random.nextInt(board.getLength());
        while (j == i) j = random.nextInt(board.getLength());
        char[] charArr = c.getCode().toCharArray();
        char temp = charArr[i];
        charArr[i] = charArr[j];
        charArr[j] = temp;
        String nou = String.valueOf(charArr);
        Code nouCodi = new Code(nou);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(nouCodi);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) nouCodi.getCode().charAt(k));
                }
                nouCodi.changeRepeatedElms(rep, colors);
            }
        }
        return nouCodi;
    }

    /** Returns an inversion of the code recieved as a parameter
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns an inversion of c*/
    public Code inverse(Code c) {
        Random random = new Random();
        int i = random.nextInt(board.getLength());
        int j = random.nextInt(board.getLength());
        if (j<i) {
            int temp = i;
            i = j;
            j = temp;
        }
        String aux = "";
        for (int k = j; k>=i ; k--) aux = aux + c.getCode().charAt(k);
        String nou = c.getCode().substring(0, i) + aux + c.getCode().substring(j+1, board.getLength());
        Code nouCodi = new Code(nou);
        if (!board.isRep()) {
            ArrayList<Integer> rep = hasRepetitions(nouCodi);
            if (rep.size() != 0) {
                Set<Integer> colors = new HashSet<>();
                for (int k = 0; k < board.getLength(); ++k) {
                    colors.add((int) nouCodi.getCode().charAt(k));
                }
                nouCodi.changeRepeatedElms(rep, colors);
            }
        }
        return nouCodi;
    }

    /** Returns an ArrayList containing the positions where there is a repeated color
     * @Pre: Singleton instance of Board has already been initialised
     * @Post: Returns an ArrayList containing the positions where there is a repeated color */
    public ArrayList<Integer> hasRepetitions(Code code) {
        ArrayList<Integer> rep = new ArrayList<>();
        Set<Character> cols = new HashSet<>();
        for (int i = 0; i < code.getLength(); ++i) {
            if (!cols.add(code.getIthElm(i))) rep.add(i);
        }
        return rep;
    }
}