package application;

import org.biojava.nbio.alignment.SimpleProfileProfileAligner;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.alignment.template.ProfilePair;
import org.biojava.nbio.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.template.Compound;
import org.biojava.nbio.core.sequence.template.Sequence;

/**
 * Klasa pomocnicza wywolujaca metody dopasowania profili
 */
public class ProfileProfileAlignment<S extends Sequence<C>, C extends Compound> {

    private final SubstitutionMatrix<C> substitutionMatrix;
    private final GapPenalty gapPenalty;

    /**
     * @param substitutionMatrix
     * @param gapPenalty
     */
    public ProfileProfileAlignment(SubstitutionMatrix<C> substitutionMatrix, GapPenalty gapPenalty) {
        this.substitutionMatrix = substitutionMatrix;
        this.gapPenalty = gapPenalty;
    }

    /**
     * Zwraca dopasowanie dwoch profili.
     *
     * @param firstProfile
     * @param secondProfile
     * @return
     */
    public SimpleProfileProfileAligner<S, C> getProfilesAlignment(Profile<S, C> firstProfile, Profile<S, C> secondProfile) {
        SimpleProfileProfileAligner aligner = new SimpleProfileProfileAligner<>(firstProfile, secondProfile, gapPenalty, substitutionMatrix);
        return aligner;
    }

    /**
     * Zwraca dopasowanie dwoch par profili.
     *
     * @param firstPair
     * @param secondPair
     * @return
     */
    public SimpleProfileProfileAligner<S, C> getProfileProfileAlignment(ProfilePair<S, C> firstPair, ProfilePair<S, C> secondPair) {
        return new SimpleProfileProfileAligner<>(firstPair, secondPair, gapPenalty, substitutionMatrix);
    }

}
