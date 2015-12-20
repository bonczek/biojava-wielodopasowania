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
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
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
    private JButton consWordButton1;
    private JButton consWordButton2;
    private JButton consWordJointButton;
    private JComboBox<String[]> matrixCombo;
    private JTextField penaltyOpenText;
    private JTextField penaltyExtendText;
    private JButton dlFastaProtSeqB1;

    MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment = null;
    Profile<ProteinSequence, AminoAcidCompound> aligmentProfile = null;
    MultiAlignment<ProteinSequence, AminoAcidCompound> multiAlignment2 = null;
    Profile<ProteinSequence, AminoAcidCompound> aligmentProfile2 = null;
    ProfileProfileAlignment<ProteinSequence, AminoAcidCompound> profileProfileAlignment = null;
    Profile<ProteinSequence, AminoAcidCompound> jointProfile = null;
    SubstitutionMatrix<AminoAcidCompound> matrix = SubstitutionMatrixHelper.getBlosum30();
    GapPenalty gapPenalty = new SimpleGapPenalty();
    private int openPenalty = 10;
    private int extendPenalty = 1;

    public MainWindow() {
        super("Wielodopasowania");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        populateMatrixComboBox();

        sequenceText.setText("ABCA\nABABA\nACCB\nCBBC");
        sequenceText2.setText("ABBB\nABCA\nAABCC\nBCCB");
        penaltyOpenText.setText("10");
        penaltyExtendText.setText("1");

        matrixCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo: zrobic to jakos bardziej elegancko moze? na szybko pojechalem - wiem, ze smiesznie wyglada :D
                switch (matrixCombo.getSelectedItem().toString()) {
                    case "Blosum 30":
                        matrix = SubstitutionMatrixHelper.getBlosum30();
                        break;
                    case "Blosum 45":
                        matrix = SubstitutionMatrixHelper.getBlosum45();
                        break;
                    case "Blosum 60":
                        matrix = SubstitutionMatrixHelper.getBlosum60();
                        break;
                    case "Blosum 75":
                        matrix = SubstitutionMatrixHelper.getBlosum75();
                        break;
                    case "Blosum 90":
                        matrix = SubstitutionMatrixHelper.getBlosum90();
                        break;
                    case "PAM 250":
                        matrix = SubstitutionMatrixHelper.getPAM250();
                        break;
                    default:
                        System.err.println("Error in combo box, entry not found");
                }
            }
        });

        penaltyOpenText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int op = -1;
                try {
                    op = Integer.parseInt(penaltyOpenText.getText());
                    if (op < 0)
                        throw null;
                }
                catch (Exception ex) {
                    resultText.setText("Kara musi być liczbą całkowitą większą od 0");
                    penaltyOpenText.setText(String.valueOf(openPenalty));
                    return;
                }
                openPenalty = op;
                gapPenalty = new SimpleGapPenalty(openPenalty, extendPenalty);
            }
        });

        penaltyExtendText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ep = -1;
                try {
                    ep = Integer.parseInt(penaltyExtendText.getText());
                    if (ep < 0)
                        throw null;
                }
                catch (Exception ex) {
                    resultText.setText("Kara musi być liczbą całkowitą większą od 0");
                    penaltyExtendText.setText(String.valueOf(extendPenalty));
                    return;
                }
                extendPenalty = ep;
                gapPenalty = new SimpleGapPenalty(openPenalty, extendPenalty);
            }
        });

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
                if (aligmentProfile == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowanie!");
                    return;
                }
                resultText.setText(getFormatedProfile(aligmentProfile));
            }
        });

        profileButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aligmentProfile2 == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowanie!");
                    return;
                }
                resultText.setText(getFormatedProfile(aligmentProfile2));
            }
        });

        jointAlignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aligmentProfile == null || aligmentProfile2 == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowania!");
                    return;
                }
                profileProfileAlignment = new ProfileProfileAlignment<>(matrix, gapPenalty);
                SimpleProfileProfileAligner<ProteinSequence, AminoAcidCompound> pa = profileProfileAlignment.getProfilesAlignment(aligmentProfile, aligmentProfile2);
                jointProfile = pa.getProfile();
                resultText.setText(jointProfile.toString());
            }
        });

        jointProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aligmentProfile == null || aligmentProfile2 == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowania!");
                    return;
                }
                resultText.setText(getFormatedProfile(jointProfile));
            }
        });

        consWordButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aligmentProfile == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowanie!");
                    return;
                }
                resultText.setText(getConsensusWord(aligmentProfile));
            }
        });

        consWordButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aligmentProfile2 == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowanie!");
                    return;
                }
                resultText.setText(getConsensusWord(aligmentProfile2));
            }
        });

        consWordJointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aligmentProfile == null || aligmentProfile2 == null) {
                    resultText.setText("Musisz najpierw stworzyc dopasowania!");
                    return;
                }
                resultText.setText(getConsensusWord(jointProfile));
            }
        });

        dlFastaProtSeqB1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String tempstr = String.valueOf(getSequenceForId(JOptionPane.showInputDialog("Podaj ID do zaimportowania")));
                    Object[] options = { "1", "2" };
                    int n = JOptionPane.showOptionDialog(null, "Dla którego wielodopasowania chcesz zaimportować sekwencję?", "Importowanie",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]);

                    if (n == 0){
                        sequenceText.append(tempstr);
                        sequenceText.append("\n");
                    }
                    else if (n == 1){
                        sequenceText2.append(tempstr);
                    }


                } catch (Exception exc) {
                    System.out.printf("Couldn't get the sequence");
                }
            }
        });
    }

    private void populateMatrixComboBox()
    {
        String[] matrices = {"Blosum 30", "Blosum 45", "Blosum 60", "Blosum 75", "Blosum 90", "PAM 250"};
        matrixCombo.setModel(new DefaultComboBoxModel(matrices));
    }

    private static String getConsensusWord(Profile<ProteinSequence, AminoAcidCompound> profile)
    {
        /**
         * Znajduje slowo konsensusowe dla danego profilu
         */
        String result = "";
        List<AminoAcidCompound> compoundList = getCompoundList(profile);

        for (int i = 1; i <= profile.getLength(); i++) {
            // dla kazdej pozycji...
            float[] weights = profile.getCompoundWeightsAt(i, compoundList);
            float maxWeight = 0;
            String dominantLetter = "-";
            for (int j = 0; j < compoundList.size(); j++) {
                // sprawdz wszystkie wagi i znajdz najwyzsza
                if (weights[j] > maxWeight) {
                    maxWeight = weights[j];
                    dominantLetter = compoundList.get(j).getShortName();
                }
            }
            result += dominantLetter;
        }
        return result;
    }

    private static List<AminoAcidCompound> getCompoundList(Profile<ProteinSequence, AminoAcidCompound> profile)
    {
        /**
         * Zwraca liste aminokwasow wystepujacych w danym profilu (wyrzuca z alfabetu wszystkie niewystepujace)
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
        return compoundList;
    }

    private static String getFormatedProfile(Profile<ProteinSequence, AminoAcidCompound> profile)
    {
        /**
         * Print do konsoli danego profilu z filtrowaniem aminokwasow i formatowaniem tabeli
         */
        String result = "";
        List<AminoAcidCompound> compoundList = getCompoundList(profile);

        // numery (tytuly kolumn)
        result += "     ";
        for (int i = 1; i <= profile.getLength(); i++) {
            result += String.format("%-5s  ", i);
        }
        result += "\n";

        // aminokwasy (tytuly wierszy) + wartosci
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
        return new MultiAlignment<>(proteinSequenceList, gapPenalty, matrix);
    }

    private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        /**
         * Pobiera z http://uniprot.org/ sekwencje protein o danym ID
         */
        uniProtId = uniProtId.toUpperCase();
        URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        //System.out.printf("%s%n",seq);
        return seq;
    }
}
