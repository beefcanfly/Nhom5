package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import com.trungtam.dao.TaiKhoanDAO;
import com.trungtam.model.TaiKhoan;

public class FullUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContent;

    public FullUI() {
        setTitle("Giao diện Login");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(200, 0));

        // ===== LOGIN =====
        JLabel lblUser = new JLabel("Tài khoản");
        JTextField txtUser = new JTextField();
        txtUser.setMaximumSize(new Dimension(360, 30));

        JLabel lblPass = new JLabel("Mật khẩu");
        JPasswordField txtPass = new JPasswordField();
        txtPass.setMaximumSize(new Dimension(360, 30));

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setMaximumSize(new Dimension(180, 35));

        // ===== MENU =====
        JButton btnHome = new JButton("Trang chủ");
        btnHome.setMaximumSize(new Dimension(180, 35));

        // ===== ADD COMPONENT =====
        leftPanel.add(lblUser);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(txtUser);

        leftPanel.add(Box.createVerticalStrut(15));

        leftPanel.add(lblPass);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(txtPass);

        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(btnLogin);

        leftPanel.add(Box.createVerticalStrut(30));

        leftPanel.add(btnHome);
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setPreferredSize(new Dimension(220, 0));
        wrapper.add(leftPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.WEST);

        // ===== MAIN CONTENT =====
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);

        JPanel homePanel = createHomePanel();
        mainContent.add(homePanel, "HOME");

        add(mainContent, BorderLayout.CENTER);

        // ===== EVENT LOGIN =====
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());

            TaiKhoanDAO dao = new TaiKhoanDAO();
            TaiKhoan tk = dao.login(username, password);

            if (tk != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

                switch (tk.getVaiTro()) {
                    case "admin":
                        new AdminUI().setVisible(true);
                        break;
                    case "giangvien":
                        new GiangVienUI().setVisible(true);
                        break;
                    case "hocvien":
                        new HocVienUI().setVisible(true);
                        break;
                }

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
            }
        });

        // ===== ENTER = LOGIN =====
        txtPass.addActionListener(e -> btnLogin.doClick());

        // ===== MENU =====
        btnHome.addActionListener(e -> cardLayout.show(mainContent, "HOME"));
    }

    // ===== HOME =====
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("UIS Cao Dang Nghe Mien Nam");
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(2, 1, 10, 10));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        content.add(createCard(
                "THÔNG BÁO",
                "Nghỉ lễ Giỗ Tổ Hùng Vương\nĐóng tiền thi lại..."
        ));

        content.add(createCard(
                "HƯỚNG DẪN",
                "Hướng dẫn đóng học phí\nCác khoản thu khác..."
        ));

        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    // ===== CARD =====
    private JPanel createCard(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.RED);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea txt = new JTextArea(content);
        txt.setEditable(false);
        txt.setFont(new Font("Arial", Font.PLAIN, 16));
        txt.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(txt, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FullUI().setVisible(true);
        });
    }
}