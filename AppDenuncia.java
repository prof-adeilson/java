import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Classe que modela os dados da denúncia
class Denuncia {
    private String tipo;
    private String vitima;
    private String agressor;
    private String local;
    private String descricao;

    public Denuncia(String tipo, String vitima, String agressor, String local, String descricao) {
        this.tipo = tipo;
        this.vitima = vitima.trim().isEmpty() ? "Anônimo" : vitima;
        this.agressor = agressor.trim().isEmpty() ? "Não informado" : agressor;
        this.local = local;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "TIPO: " + tipo + "\n" +
                "VÍTIMA: " + vitima + "\n" +
                "AGRESSOR: " + agressor + "\n" +
                "LOCAL: " + local + "\n" +
                "DESCRIÇÃO: " + descricao + "\n" +
                "-----------------------------------------\n";
    }
}

// Classe principal que gera a interface gráfica
public class AppDenuncia extends JFrame {
    private ArrayList<Denuncia> listaDenuncias = new ArrayList<>();

    // Componentes da interface
    private JComboBox<String> comboTipo;
    private JTextField txtVitima, txtAgressor, txtLocal;
    private JTextArea txtDescricao, txtAreaRelatorio;
    private JButton btnSalvar, btnListar, btnEstatisticas;

    public AppDenuncia() {
        // Configurações básicas da janela principal
        setTitle("Sistema de Denúncias Contra o Bullying");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINÉL DE FORMULÁRIO (ENTRADA DE DADOS) ---
        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 5, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painelFormulario.add(new JLabel("Tipo de Bullying:"));
        String[] tipos = { "Verbal", "Físico", "Social/Psicológico", "Cyberbullying", "Outro" };
        comboTipo = new JComboBox<>(tipos);
        painelFormulario.add(comboTipo);

        painelFormulario.add(new JLabel("Seu Nome (Deixe em branco p/ Anônimo):"));
        txtVitima = new JTextField();
        painelFormulario.add(txtVitima);

        painelFormulario.add(new JLabel("Nome do Agressor (Se souber):"));
        txtAgressor = new JTextField();
        painelFormulario.add(txtAgressor);

        painelFormulario.add(new JLabel("Local do Ocorrido:"));
        txtLocal = new JTextField();
        painelFormulario.add(txtLocal);

        painelFormulario.add(new JLabel("Descrição dos Fatos:"));
        txtDescricao = new JTextArea(3, 20);
        txtDescricao.setLineWrap(true);
        painelFormulario.add(new JScrollPane(txtDescricao));

        // --- PAINÉL DE BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout());
        btnSalvar = new JButton("Enviar Denúncia");
        btnListar = new JButton("Listar Ocorrências");
        btnEstatisticas = new JButton("Ver Estatísticas");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnEstatisticas);

        // Agrupar formulário e botões no topo da janela
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        // --- PAINÉL DE EXIBIÇÃO (RELATÓRIOS) ---
        txtAreaRelatorio = new JTextArea();
        txtAreaRelatorio.setEditable(false);
        txtAreaRelatorio.setBackground(new Color(245, 245, 245));
        JScrollPane scrollRelatorio = new JScrollPane(txtAreaRelatorio);
        scrollRelatorio.setBorder(BorderFactory.createTitledBorder("Painel de Visualização (Coordenação)"));

        // Adicionar painéis na janela principal
        add(painelSuperior, BorderLayout.NORTH);
        add(scrollRelatorio, BorderLayout.CENTER);

        // --- AÇÕES DOS BOTÕES (LISTENERS) ---

        // Ação: Salvar Denúncia
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtLocal.getText().trim().isEmpty() || txtDescricao.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o Local e a Descrição.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Denuncia d = new Denuncia(
                        comboTipo.getSelectedItem().toString(),
                        txtVitima.getText(),
                        txtAgressor.getText(),
                        txtLocal.getText(),
                        txtDescricao.getText());

                listaDenuncias.add(d);
                JOptionPane.showMessageDialog(null, "Denúncia registrada com sucesso e enviada para triagem!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        // Ação: Listar Denúncias
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaDenuncias.isEmpty()) {
                    txtAreaRelatorio.setText("Nenhum relato registrado no sistema.");
                    return;
                }

                StringBuilder sb = new StringBuilder("=== RELATO DE DENÚNCIAS REGISTRADAS ===\n\n");
                for (Denuncia d : listaDenuncias) {
                    sb.append(d);
                }
                txtAreaRelatorio.setText(sb.toString());
            }
        });

        // Ação: Exibir Estatísticas
        btnEstatisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaDenuncias.isEmpty()) {
                    txtAreaRelatorio.setText("Dados insuficientes para gerar métricas.");
                    return;
                }

                int v = 0, f = 0, s = 0, c = 0, o = 0;
                for (Denuncia d : listaDenuncias) {
                    switch (d.getTipo()) {
                        case "Verbal":
                            v++;
                            break;
                        case "Físico":
                            f++;
                            break;
                        case "Social/Psicológico":
                            s++;
                            break;
                        case "Cyberbullying":
                            c++;
                            break;
                        default:
                            o++;
                    }
                }

                String estatisticas = "=== MÉTRICAS DE INCIDENTES ===\n\n" +
                        "Total de Ocorrências: " + listaDenuncias.size() + "\n\n" +
                        "• Verbal: " + v + "\n" +
                        "• Físico: " + f + "\n" +
                        "• Social/Psicológico: " + s + "\n" +
                        "• Cyberbullying: " + c + "\n" +
                        "• Outros: " + o + "\n";
                txtAreaRelatorio.setText(estatisticas);
            }
        });
    }

    private void limparCampos() {
        comboTipo.setSelectedIndex(0);
        txtVitima.setText("");
        txtAgressor.setText("");
        txtLocal.setText("");
        txtDescricao.setText("");
    }

    public static void main(String[] args) {
        // Garante que a interface gráfica rode de forma segura na thread correta
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppDenuncia().setVisible(true);
            }
        });
    }
}
