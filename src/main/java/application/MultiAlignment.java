package application;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.sequence.template.Compound;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.util.ConcurrencyTools;

import java.util.List;

public class MultiAlignment<S extends Sequence<C>, C extends Compound> {

    private final Profile<S, C> multialignmentProfile;

    /**
     * Tworzy wielodopasowanie dla danej listy sekwencji.
     * Rezultat jest zapisany jako profil odpowiedniego typu zapisany w zmiennej.
     *
     * @param sequences lista sekwencji
     * @param settings  opcjonalne parametry wielodopasowania takiej jak: PairwiseSequenceScorerType, GapPenalty, SubstitutionMatrix i ProfileProfileAlignerType
     */
    public MultiAlignment(List<S> sequences, Object... settings) {
        multialignmentProfile = Alignments.getMultipleSequenceAlignment(sequences, settings);
        ConcurrencyTools.shutdown();
    }

    public Profile<S, C> getMultialignmentProfile() {
        return multialignmentProfile;
    }
}
