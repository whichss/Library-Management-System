package model;

import java.time.LocalDate;

public class Book {
    private String barcode;
    private String callNumber;
    private String title;
    private String author;
    private String publisher;
    private String publicationYear;
    private String category;
    private boolean isAvailable;
    private String status;
    private LocalDate purchaseDate;
    private int price;
    private String note;
    
    // 신규 도서 생성자
    public Book(String title, String author, String publisher, String publicationYear, 
                String category, int price) {
        this.barcode = "";
        this.callNumber = generateCallNumber(category, author); // 자동 생성
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.category = category;
        this.isAvailable = true;
        this.status = "정상";
        this.purchaseDate = LocalDate.now();
        this.price = price;
        this.note = "";
    }
    
    // 전체 정보 생성자 (CSV용)
    public Book(String barcode, String callNumber, String title, String author, 
                String publisher, String publicationYear, String category,
                boolean isAvailable, String status, LocalDate purchaseDate, 
                int price, String note) {
        this.barcode = barcode;
        this.callNumber = callNumber;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.category = category;
        this.isAvailable = isAvailable;
        this.status = status;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.note = note;
    }
    
    // 청구기호 자동 생성 메서드
    private String generateCallNumber(String category, String author) {
        String categoryCode = getCategoryCode(category);
        String authorCode = getAuthorCode(author);
        return categoryCode + "-" + authorCode;
    }
    
    // 카테고리별 분류 번호
    private String getCategoryCode(String category) {
        switch (category) {
            case "문학": return "813";
            case "과학": return "400";
            case "역사": return "900";
            case "철학": return "100";
            case "예술": return "700";
            case "컴퓨터": case "프로그래밍": return "005";
            case "경제": return "320";
            case "교육": return "370";
            default: return "000";
        }
    }
    
    // 저자명 코드 생성 (성 첫글자 + 이름 첫글자)
    private String getAuthorCode(String author) {
        if (author.length() >= 2) {
            return author.substring(0, 1) + author.substring(1, 2);
        }
        return author + "1";
    }
    
    // Getters
    public String getBarcode() { return barcode; }
    public String getCallNumber() { return callNumber; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getPublicationYear() { return publicationYear; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public String getStatus() { return status; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public int getPrice() { return price; }
    public String getNote() { return note; }
    
    // Setters
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public void setCallNumber(String callNumber) { this.callNumber = callNumber; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setPublicationYear(String publicationYear) { this.publicationYear = publicationYear; }
    public void setCategory(String category) { this.category = category; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setStatus(String status) { this.status = status; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public void setPrice(int price) { this.price = price; }
    public void setNote(String note) { this.note = note; }
    
    public String toCsvString() {
        return barcode + "," + callNumber + "," + title + "," + author + "," + 
               publisher + "," + publicationYear + "," + category + "," + 
               isAvailable + "," + status + "," + purchaseDate + "," + price + "," + note;
    }
    
    public boolean canBorrow() {
        return isAvailable && status.equals("정상");
    }
}
