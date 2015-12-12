package application;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Przygotowanie sekwencji
        // todo: sprawdzic co to wlasciwie za ID, wypadaloby dorzucic troche wiecej
        String[] ids = new String[] {"Q21691", "A8WS47", "O48771"};
        List<ProteinSequence> proteinSequenceList = new ArrayList<ProteinSequence>();
        for (String id : ids) {
            try {
                proteinSequenceList.add(getSequenceForId(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Wielodopasowanie
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
