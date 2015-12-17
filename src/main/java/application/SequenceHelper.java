package application;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SequenceHelper {

    /**
     * Pobiera z http://uniprot.org/ sekwencje białek o danym ID
     *
     * @param uniProtId id sekwencji na stronie
     * @return obiekt sekwencji białka
     */
    public static ProteinSequence getProteinSequenceFromWebsite(String uniProtId) throws IOException {
        URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        InputStream protStream = uniprotFasta.openStream();
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        protStream.close();
        return seq;
    }
}
