public class Book {
    public String title;
    public String publicationDate;
    public String author;
    public String language;

    public Book(String title, String year, String author, String language) {
        this.title = title;
        this.publicationDate = year;
        this.author = author;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", publication date='" + publicationDate + '\'' +
                ", author='" + author + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}

