package application;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        MainWindow mw = new MainWindow();

        Options consoleOptions = prepareApplicationOptions();

        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(consoleOptions, args);
            if (line.hasOption("r")) {
                List<ProteinSequence> proteinSequenceList = new ArrayList<>();
                List<String> rawSequences = Arrays.asList(line.getOptionValues("r"));
                for (String seq : rawSequences) {
                    try {
                        proteinSequenceList.add(new ProteinSequence(seq));
                    } catch (CompoundNotFoundException e) {
                        System.err.println(String.format("Niepoprawne sekwencje bialek\n%s", e.getMessage()));
                    }
                }
                createProteinMultialignment(proteinSequenceList);
            }
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

        // Przygotowanie sekwencji (tymczasowo nieuzywane)
        // todo: sprawdzic co to wlasciwie za ID, wypadaloby dorzucic troche wiecej
        // Te sa wybrane losowo
        // P31946 - Homo Sapiens (Human)
        // Q9D020 - Mus musculus (Mouse)
        // Q8MJC8 - Gorilla gorilla gorilla (Western lowland gorilla)
        // P68194 - Panthera tigris sumatrae (Sumatran tiger)
        // Q1HKA1 - Canis lupus (Gray wolf)
        // P02129 - Crocodylus niloticus (Nile crocodile)
        // P35031 - Salmo salar (Atlantic salmon)
        //
        // String[] ids = new String[] {"P31946", "Q9D020", "Q8MJC8", "P68194", "Q1HKA1", "P02129", "P35031"};
        //
        // Te wszystkie dotycza Hemoglobiny (Hemoglobun subunit Beta)
        // Jak jest ich duzo to az tak wszystkie do siebie nie sa podobne jednak
        // P68871 - Homo sapiens (Human)
        // P02112 - Gallus gallus (Chicken)
        // P02062 - Equus caballus (Horse)
        // P02067 - Sus scrofa (Pig)
        // P02075 - Ovis aries (Sheep)
        // P80044 - Trematomus bernacchii (Emerald rockcod)
        // P02070 - Bos taurus (Bovine)
        // P18983 - Ailuropoda melanoleuca (Giant panda)
        // P02117 - Anser anser anser (Western greylag goose)
        // P02095 - Cavia porcellus (Guinea pig)
        // P02053 - Eulemur fulvus fulvus (Brown lemur)
        // P68044 - Mustela putorius furo (European domestic ferret)
        // P68873 - Pan troglodytes (Chimpanzee)
        // Q91473 - Salmo salar (Atlantic salmon)
        // P02130 - Alligator mississippiensis (American alligator)
        // P02122 - Aquila chrysaetos (Golden eagle)
        // P09422 - Bison bonasus (European bison)
        // P60525 - Canis latrans (Coyote)
        // Q10733 - Caretta caretta (Loggerhead sea turtle)
        //
        // String[] ids = new String[] {"P68871", "P02112", "P02062", "P02067", "P02075", "P80044", "P02070",
        //                              "P18983", "P02117", "P02095", "P02053", "P68044", "P68873", "Q91473",
        //                              "P02130", "P02122", "P09422", "P60525", "Q10733"};
        /*
        String[] ids = new String[] {"Q21691", "A8WS47", "O48771"};
        List<ProteinSequence> proteinSequenceList = new ArrayList<ProteinSequence>();
        for (String id : ids) {
            try {
                proteinSequenceList.add(getSequenceForId(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */

    }

    private static void createProteinMultialignment(List<ProteinSequence> proteinSequences) {
        // Wielodopasowanie
        // todo: Przy tworzeniu profilu pomija spacje - sprawdzic, czy da sie ja uwzglednic (drugi argument Alignments.getMultipleSequenceAlignment(...) ?)
        MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment = new MultiAlignment<>(proteinSequences);
        Profile<ProteinSequence, AminoAcidCompound> aligmentProfile = multiAlignment.getMultialignmentProfile();

        // Print do konsoli (tymczasowo)
        // todo: Przeniesc do GUI? (czy zostaje w konsoli?)
        if (aligmentProfile != null) {
            System.out.printf("Dopasowanie:%n%s%n", aligmentProfile);
            System.out.print("Profil:\n");
            PrintFormatedProfile(aligmentProfile);
        }
        // todo: Dodac dopasowanie sekwencji do profilu? Co jeszcze?
    }

    private static void PrintFormatedProfile(Profile<ProteinSequence, AminoAcidCompound> profile)
    {
        /**
         * Print do konsoli danego profilu z filtrowaniem aminokwasow i formatowaniem tabeli
         */
        List<AminoAcidCompound> compoundList = new ArrayList<AminoAcidCompound>();
        for (int i = 1; i <= profile.getLength(); i++) {
            List<AminoAcidCompound> compoundListAtPosition = profile.getCompoundsAt(i);
            for (AminoAcidCompound comp : compoundListAtPosition) {
                if (comp.getShortName().equals("-")) continue; // Ignoruj znak spacji
                if (!compoundList.contains(comp))
                    compoundList.add(comp);
            }
        }
        Collections.sort(compoundList, new Comparator<AminoAcidCompound>() {
            public int compare(AminoAcidCompound comp1, AminoAcidCompound comp2)
            {
                return  comp1.getShortName().compareTo(comp2.getShortName());
            }
        });
        System.out.print("     ");
        for (AminoAcidCompound comp : compoundList) {
            System.out.print(comp.getShortName() + "     ");
        }
        System.out.print("\n");
        for (int i = 1; i <= profile.getLength(); i++) {
            float[] weights = profile.getCompoundWeightsAt(i, compoundList);
            System.out.print(i + ": ");
            for (float weight : weights) {
                System.out.format("%.3f ", weight);
            }
            System.out.print("\n");
        }
    }

    private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        /**
         * Pobiera z http://uniprot.org/ sekwencje protein o danym ID
         */
        URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        System.out.printf("%s%n",seq);
        return seq;
    }

    private static Options prepareApplicationOptions() {
        Options options = new Options();
        Option rawSequence = OptionBuilder.withArgName("RawSequences")
                .withLongOpt("raw-sequences")
                .isRequired(false)
                .hasArgs()
                .withValueSeparator(' ')
                .withDescription("wczytanie sekwencji nukleotydowych podanych przez uzytkownika")
                .create("r");


        options.addOption(rawSequence);
        return options;
    }
}
