import controller.MainController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainController().start();
            System.out.println("도서관 관리 시스템 시작");
            System.out.println("기본 관리자: admin/admin123");
        });
    }
}
    