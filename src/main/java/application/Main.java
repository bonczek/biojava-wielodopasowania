package application;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.util.ConcurrencyTools;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Przygotowanie sekwencji (tymczasowo nieuzywane)
        // todo: sprawdzic co to wlasciwie za ID, wypadaloby dorzucic troche wiecej
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

        // Utworzenie sekwencji ze stringow, tymczasowy przyklad z wykladu
        List<ProteinSequence> proteinSequenceList = new ArrayList<ProteinSequence>();
        String[] seqStrings = new String[] {
                "ABCA",
                "ABABA",
                "ACCB",
                "CBBC"
        };
        for (String seq : seqStrings) {
            try {
                proteinSequenceList.add(new ProteinSequence(seq));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Wielodopasowanie
        // todo: Przy tworzeniu profilu pomija spacje - sprawdzic, czy da sie ja uwzglednic (drugi argument Alignments.getMultipleSequenceAlignment(...) ?)
        Profile<ProteinSequence, AminoAcidCompound> profile = null;
        try {
            profile = multipleSequenceAlignment(proteinSequenceList);
        } catch (Exception e){
            e.printStackTrace();
        }

        // Print do konsoli (tymczasowo)
        // todo: Przeniesc do GUI? (czy zostaje w konsoli?)
        if (profile != null)
        {
            System.out.printf("Dopasowanie:%n%s%n", profile);
            System.out.print("Profil:\n");
            PrintFormatedProfile(profile);
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
        for (AminoAcidCompound aCompoundList : compoundList) {
            System.out.print(aCompoundList.getShortName() + "     ");
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

    private static Profile<ProteinSequence, AminoAcidCompound> multipleSequenceAlignment(List<ProteinSequence> lst) throws Exception {
        /**
         * Tworzy wielodopasowanie dla danej listy sekwencji protein
         */
        Profile<ProteinSequence, AminoAcidCompound> profile = Alignments.getMultipleSequenceAlignment(lst);
        ConcurrencyTools.shutdown();
        return profile;
    }

    private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        /**
         * Pobiera z http://uniprot.org/ sekwencje protein o danym ID
         */
        URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        System.out.printf("id : %s %s%n%s%n", uniProtId, seq, seq.getOriginalHeader());
        return seq;
    }
}
