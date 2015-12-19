package application;

import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.SubstitutionMatrixHelper;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.alignment.template.ProfilePair;
import org.biojava.nbio.alignment.template.ProfileProfileAligner;
import org.biojava.nbio.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileProfileAlignmentTest {

    private static final GapPenalty DEFAULT_GAP_PENALTY = new SimpleGapPenalty();

    private static final SubstitutionMatrix<AminoAcidCompound> DEFAULT_SUBSTITUTION_MATRIX = SubstitutionMatrixHelper.getBlosum62();

    @Test
    public void testGetTwoProfilesAlignment() throws Exception {
        List<Profile<ProteinSequence, AminoAcidCompound>> profiles = prepareFourProfiles();
        ProfileProfileAlignment<ProteinSequence, AminoAcidCompound> profileAlignment = new ProfileProfileAlignment<>(DEFAULT_SUBSTITUTION_MATRIX, DEFAULT_GAP_PENALTY);

        ProfileProfileAligner<ProteinSequence, AminoAcidCompound> profileAligner = profileAlignment.getProfilesAlignment(profiles.get(0), profiles.get(1));

        printAlignmentResult(profileAligner);

        System.out.println(String.format("Clustalw:\n%s\n", profileAligner.getPair().toString(Profile.StringFormat.CLUSTALW)));
        System.out.println(String.format("Fasta:\n%s\n", profileAligner.getPair().toString(Profile.StringFormat.FASTA)));
        System.out.println(String.format("MSF:\n%s\n", profileAligner.getPair().toString(Profile.StringFormat.MSF)));
    }

    @Test
    public void testGetFourProfilesAlignment() throws Exception {
        List<Profile<ProteinSequence, AminoAcidCompound>> profiles = prepareFourProfiles();
        ProfileProfileAlignment<ProteinSequence, AminoAcidCompound> profileAlignment = new ProfileProfileAlignment<>(DEFAULT_SUBSTITUTION_MATRIX, DEFAULT_GAP_PENALTY);

        ProfilePair<ProteinSequence, AminoAcidCompound> firstProfilePair = profileAlignment.getProfilesAlignment(profiles.get(0), profiles.get(1)).getPair();
        ProfilePair<ProteinSequence, AminoAcidCompound> secondProfilePair = profileAlignment.getProfilesAlignment(profiles.get(2), profiles.get(3)).getPair();

        ProfileProfileAligner<ProteinSequence, AminoAcidCompound> aligner = profileAlignment.getProfileProfileAlignment(firstProfilePair, secondProfilePair);

        printAlignmentResult(aligner);

        Profile<ProteinSequence, AminoAcidCompound> profile = aligner.getProfile();
        System.out.println(String.format("Clustalw:\n%s\n", profile.toString(Profile.StringFormat.CLUSTALW)));
    }

    private List<Profile<ProteinSequence, AminoAcidCompound>> prepareFourProfiles() {
        List<Profile<ProteinSequence, AminoAcidCompound>> profiles = new ArrayList<>();

        List<ProteinSequence> firstSequenceList = prepareProteinSequences(Arrays.asList("Q6GZX4", "Q6GZX3"));
        profiles.add(prepareAlignmentProfile(firstSequenceList));
        List<ProteinSequence> secondSequenceList = prepareProteinSequences(Arrays.asList("Q197F8", "Q197F7"));
        profiles.add(prepareAlignmentProfile(secondSequenceList));
        List<ProteinSequence> thirdSequenceList = prepareProteinSequences(Arrays.asList("Q6GZX2", "Q6GZX1"));
        profiles.add(prepareAlignmentProfile(thirdSequenceList));
        List<ProteinSequence> fourthSequenceList = prepareProteinSequences(Arrays.asList("Q197F5", "Q6GZX0"));
        profiles.add(prepareAlignmentProfile(fourthSequenceList));

        return profiles;
    }

    private Profile<ProteinSequence, AminoAcidCompound> prepareAlignmentProfile(List<ProteinSequence> sequences) {
        MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment = new MultiAlignment<>(sequences);
        return multiAlignment.getMultialignmentProfile();
    }

    private List<ProteinSequence> prepareProteinSequences(List<String> sequencesId) {
        List<ProteinSequence> proteinSequenceList = new ArrayList<>();
        for (String protId : sequencesId) {
            try {
                proteinSequenceList.add(SequenceHelper.getProteinSequenceFromWebsite(protId));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return proteinSequenceList;
    }

    private void printAlignmentResult(ProfileProfileAligner aligner) {
        System.out.println(String.format("Score: %f", aligner.getScore()));
        System.out.println(String.format("Distance: %f", aligner.getDistance()));
        System.out.println(String.format("Max score: %f", aligner.getMaxScore()));
        System.out.println(String.format("Min score: %f", aligner.getMinScore()));
    }
}