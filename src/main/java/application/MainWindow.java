package application;

import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.SimpleProfileProfileAligner;
import org.biojava.nbio.alignment.SubstitutionMatrixHelper;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vimph on 2015-12-20.
 */
public class MainWindow extends JFrame {

    private JPanel rootPanel;
    private JTextArea sequenceText;
    private JButton multiAlignmentButton;
    private JTextArea alignmentText;
    private JTextArea sequenceText2;
    private JButton multiAlignmentButton2;
    private JTextArea alignmentText2;
    private JTextArea resultText;
    private JButton profileButton2;
    private JButton profileButton1;
    private JButton jointAlignmentButton;
    private JButton jointProfilesButton;

    MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment;
    Profile<ProteinSequence, AminoAcidCompound> aligmentProfile;
    MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment2;
    Profile<ProteinSequence, AminoAcidCompound> aligmentProfile2;
    ProfileProfileAlignment<ProteinSequence, AminoAcidCompound> profileProfileAlignment;

    public MainWindow() {
        super("Wielodopasowania");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        sequenceText.setText("ABCA\nABABA\nACCB\nCBBC");
        sequenceText2.setText("ABBB\nABCA\nAABCC\nBCCB");

        multiAlignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiAlignment = createMultiAlignmentFromString(sequenceText.getText());
                aligmentProfile = multiAlignment.getMultialignmentProfile();
                alignmentText.setText(aligmentProfile.toString());
            }
        });

        multiAlignmentButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiAlignment2 = createMultiAlignmentFromString(sequenceText2.getText());
                aligmentProfile2 = multiAlignment2.getMultialignmentProfile();
                alignmentText2.setText(aligmentProfile2.toString());
            }
        });

        profileButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.setText(getFormatedProfile(aligmentProfile));
            }
        });

        profileButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.setText(getFormatedProfile(aligmentProfile2));
            }
        });

        jointAlignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubstitutionMatrix<AminoAcidCompound> matrix = SubstitutionMatrixHelper.getBlosum30();
                GapPenalty penalty = new SimpleGapPenalty();
                profileProfileAlignment = new ProfileProfileAlignment<>(matrix, penalty);
                SimpleProfileProfileAligner<ProteinSequence, AminoAcidCompound> pa = profileProfileAlignment.getProfilesAlignment(aligmentProfile, aligmentProfile2);
                resultText.setText(pa.getProfile().toString());
            }
        });

        jointProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubstitutionMatrix<AminoAcidCompound> matrix = SubstitutionMatrixHelper.getBlosum30();
                GapPenalty penalty = new SimpleGapPenalty();
                profileProfileAlignment = new ProfileProfileAlignment<>(matrix, penalty);
                SimpleProfileProfileAligner<ProteinSequence, AminoAcidCompound> pa = profileProfileAlignment.getProfilesAlignment(aligmentProfile, aligmentProfile2);
                resultText.setText(getFormatedProfile(pa.getProfile()));
            }
        });
    }

    private static String getFormatedProfile(Profile<ProteinSequence, AminoAcidCompound> profile)
    {
        /**
         * Print do konsoli danego profilu z filtrowaniem aminokwasow i formatowaniem tabeli
         */
        String result = "";
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

        // numery
        result += "     ";
        for (int i = 1; i <= profile.getLength(); i++) {
            result += String.format("%-5s  ", i);
        }
        result += "\n";

        // aminokwasy + wartosci
        for (int i = 0; i < compoundList.size(); i++) {
            result += compoundList.get(i).getShortName() + "  ";
            for (int j = 1; j <= profile.getLength(); j++) {
                float[] weights = profile.getCompoundWeightsAt(j, compoundList);
                result += String.format("%.3f  ", weights[i]);
            }
            result += "\n";
        }
        return result;
    }

    private MultiAlignment<ProteinSequence, AminoAcidCompound> createMultiAlignmentFromString (String sequences) {
        List<ProteinSequence> proteinSequenceList = new ArrayList<>();
        String[] seqList = sequences.split("\n");
        for (String s : seqList) {
            try {
                proteinSequenceList.add(new ProteinSequence(s));
            } catch (CompoundNotFoundException e) {
                System.err.println(String.format("Niepoprawne sekwencje bialek\n%s", e.getMessage()));
            }
        }
        return new MultiAlignment<>(proteinSequenceList);
    }
}
