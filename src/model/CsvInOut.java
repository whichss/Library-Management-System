package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvInOut {
    private static final String USER_FILE = "data/users.csv";
    private static final String BOOK_FILE = "data/books.csv";
    private static final String LOAN_FILE = "data/loans.csv";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public CsvInOut() {
        new File("data").mkdir();
    }
    
    // 사용자 읽기
    public List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(USER_FILE), StandardCharsets.UTF_8))) {
            reader.readLine(); // 헤더 스킵
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String userId = parts[0];
                    String name = parts[1];
                    String password = parts[2];
                    String userType = parts[3];
                    
                    // 날짜 파싱
                    LocalDate signupDate = null;
                    try {
                        signupDate = LocalDate.parse(parts[4], DATE_FORMAT);
                    } catch (Exception e) {
                        signupDate = LocalDate.now();
                    }
                    
                    User user;
                    if (userType.equals("ADMIN")) {
                        user = new Admin(userId, name, password);
                        user.setSignupDate(signupDate);
                        if (parts.length >= 6) {
                            try {
                                ((Admin) user).setBorrowedCount(Integer.parseInt(parts[5]));
                            } catch (NumberFormatException ex) {
                                ((Admin) user).setBorrowedCount(0);
                            }
                        }
                    } else {
                        user = new Member(userId, name, password);
                        user.setSignupDate(signupDate);
                        if (parts.length >= 6) {
                            try {
                                ((Member) user).setBorrowedCount(Integer.parseInt(parts[5]));
                            } catch (NumberFormatException ex) {
                                ((Member) user).setBorrowedCount(0);
                            }
                        }
                        if (parts.length >= 7) {
                            try {
                                ((Member) user).setOverdueCount(Integer.parseInt(parts[6]));
                            } catch (NumberFormatException ex) {
                                ((Member) user).setOverdueCount(0);
                            }
                        }
                        if (parts.length >= 8) {
                            try {
                                ((Member) user).setSuspended(Boolean.parseBoolean(parts[7]));
                            } catch (Exception ex) {
                                ((Member) user).setSuspended(false);
                            }
                        }
                    }
                    users.add(user);
                }
            }
        } catch (IOException e) {
            createUserFile();
            // 기본 관리자 계정 추가
            Admin admin = new Admin("admin", "관리자", "admin123");
            admin.setSignupDate(LocalDate.now());
            users.add(admin);
        }
        return users;
    }
    
    // 사용자 저장
    public void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(USER_FILE), StandardCharsets.UTF_8))) {
            writer.println("userId,name,password,userType,signupDate,borrowedCount,overdueCount,isSuspended");
            for (User user : users) {
                writer.print(user.toCsvString());
                if (user instanceof Admin) {
                    Admin admin = (Admin) user;
                    writer.println("," + admin.getBorrowedCount() + ",,");
                } else if (user instanceof Member) {
                    Member member = (Member) user;
                    writer.println("," + member.getBorrowedCount() + 
                                 "," + member.getOverdueCount() + 
                                 "," + member.isSuspended());
                } else {
                    writer.println(",0,,");
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 파일 저장 실패: " + e.getMessage());
        }
    }
    
    // 도서 읽기
    public List<Book> readBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(BOOK_FILE), StandardCharsets.UTF_8))) {
            reader.readLine(); // 헤더 스킵
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 12) {
                    try {
                        LocalDate purchaseDate = LocalDate.parse(parts[9], DATE_FORMAT);
                        books.add(new Book(parts[0], parts[1], parts[2], parts[3], 
                                         parts[4], parts[5], parts[6],
                                         Boolean.parseBoolean(parts[7]), parts[8], 
                                         purchaseDate, Integer.parseInt(parts[10]), parts[11]));
                    } catch (Exception e) {
                        System.err.println("도서 데이터 파싱 오류: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            createBookFile();
        }
        return books;
    }
    
    // 도서 저장
    public void saveBooks(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(BOOK_FILE), StandardCharsets.UTF_8))) {
            writer.println("barcode,callNumber,title,author,publisher,publicationYear,category,isAvailable,status,purchaseDate,price,note");
            for (Book book : books) {
                writer.println(book.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("도서 파일 저장 실패: " + e.getMessage());
        }
    }
    
    // 대출 읽기
    public List<Loan> readLoans() {
        List<Loan> loans = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(LOAN_FILE), StandardCharsets.UTF_8))) {
            reader.readLine(); // 헤더 스킵
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    loans.add(new Loan(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            createLoanFile();
        }
        return loans;
    }
    
    // 대출 저장
    public void saveLoans(List<Loan> loans) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(LOAN_FILE), StandardCharsets.UTF_8))) {
            writer.println("loanId,userId,bookId,loanDate,dueDate,returnDate");
            for (Loan loan : loans) {
                writer.println(loan.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("대출 파일 저장 실패: " + e.getMessage());
        }
    }
    
    // 빈 파일 생성
    private void createUserFile() {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(USER_FILE), StandardCharsets.UTF_8))) {
            writer.println("userId,name,password,userType,signupDate,borrowedCount,overdueCount,isSuspended");
            writer.println("admin,관리자,admin123,ADMIN," + LocalDate.now() + ",0,,");
        } catch (IOException e) {
            System.err.println("사용자 파일 생성 실패: " + e.getMessage());
        }
    }
    
    private void createBookFile() {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(BOOK_FILE), StandardCharsets.UTF_8))) {
            writer.println("barcode,callNumber,title,author,publisher,publicationYear,category,isAvailable,status,purchaseDate,price,note");
            writer.println("1000001,005-남궁,자바의 정석,남궁성,도우출판,2023,프로그래밍,true,정상," + LocalDate.now() + ",45000,");
            writer.println("1000002,005-신용,이것이 자바다,신용권,한빛미디어,2023,프로그래밍,true,정상," + LocalDate.now() + ",36000,");
        } catch (IOException e) {
            System.err.println("도서 파일 생성 실패: " + e.getMessage());
        }
    }
    
    private void createLoanFile() {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(LOAN_FILE), StandardCharsets.UTF_8))) {
            writer.println("loanId,userId,bookId,loanDate,dueDate,returnDate");
        } catch (IOException e) {
            System.err.println("대출 파일 생성 실패: " + e.getMessage());
        }
    }
}