package application;


import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.util.ConcurrencyTools;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiAlignmentTest {

    @Test
    public void testGetMultialignmentProfile_withDefaultSettings() throws Exception {
        List<ProteinSequence> sequences = prepareSequences();

        MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment = new MultiAlignment<>(sequences);
        Profile<ProteinSequence, AminoAcidCompound> profile = multiAlignment.getMultialignmentProfile();

        ConcurrencyTools.shutdown();
        System.out.println(String.format("Clustalw:\n%s\n", profile.toString(Profile.StringFormat.CLUSTALW)));
        System.out.println(String.format("Fasta:\n%s\n", profile.toString(Profile.StringFormat.FASTA)));
        System.out.println(String.format("Fasta:\n%s\n", profile.toString(Profile.StringFormat.MSF)));
    }

    @Test
    public void testGetMultialignmentProfile_withChangedScorer() throws Exception {
        List<ProteinSequence> sequences = prepareSequences();

        //@todo dowiedzieć się co to zmienia i co oznaczają te różne opcje
        //domyślnie jest Alignments.PairwiseSequenceScorerType.GLOBAL_IDENTITIES
        MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment = new MultiAlignment<>(sequences, Alignments.PairwiseSequenceScorerType.GLOBAL_SIMILARITIES);
        Profile<ProteinSequence, AminoAcidCompound> profile = multiAlignment.getMultialignmentProfile();

        ConcurrencyTools.shutdown();
        System.out.println(String.format("Clustalw:\n%s\n", profile.toString(Profile.StringFormat.CLUSTALW)));
    }

    @Test
    public void testGetMultialignmentProfile_withChangedGapPenalty() throws Exception {
        List<ProteinSequence> sequences = prepareSequences();
        GapPenalty gapPenalty = new SimpleGapPenalty(10, 20);

        //@todo dowiedzieć się o tych wartościa gop i gep w SimpleGapPenalty
        MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment = new MultiAlignment<>(sequences, gapPenalty);
        Profile<ProteinSequence, AminoAcidCompound> profile = multiAlignment.getMultialignmentProfile();

        ConcurrencyTools.shutdown();
        System.out.println(String.format("Clustalw:\n%s\n", profile.toString(Profile.StringFormat.CLUSTALW)));
    }

    //@todo konfiguracja z SubstitutionMatrix
    //@todo konfiguracja z ProfileProfileAlignerType


    private List<ProteinSequence> prepareSequences() {
        String[] proteinIds = new String[]{"Q21691", "A8WS47", "O48771"};
        List<ProteinSequence> proteinSequenceList = new ArrayList<>();
        for (String id : proteinIds) {
            try {
                proteinSequenceList.add(SequenceHelper.getProteinSequenceFromWebsite(id));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return proteinSequenceList;
    }
}