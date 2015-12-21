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
//         P02117 - Anser anser anser (Western greylag goose)
//         P02095 - Cavia porcellus (Guinea pig)
//         P02053 - Eulemur fulvus fulvus (Brown lemur)
//         P68044 - Mustela putorius furo (European domestic ferret)
//         P68873 - Pan troglodytes (Chimpanzee)
//         Q91473 - Salmo salar (Atlantic salmon)
//         P02130 - Alligator mississippiensis (American alligator)
//         P02122 - Aquila chrysaetos (Golden eagle)
//         P09422 - Bison bonasus (European bison)
        // P60525 - Canis latrans (Coyote)
        // Q10733 - Caretta caretta (Loggerhead sea turtle)
        //
    }

}
