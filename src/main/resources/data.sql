MERGE INTO BOOK_ENTITY (id, title, author, publication_year, pages)
KEY(id)
VALUES
    ('11111111-1111-1111-1111-111111111111', '1984', 'George Orwell', '1949', 328),
    ('22222222-2222-2222-2222-222222222222', 'Brave New World', 'Aldous Huxley', '1932', 311),
    ('33333333-3333-3333-3333-333333333333', 'Fahrenheit 451', 'Ray Bradbury', '1953', 249),
    ('44444444-4444-4444-4444-444444444444', 'To Kill a Mockingbird', 'Harper Lee', '1960', 281),
    ('55555555-5555-5555-5555-555555555555', 'The Great Gatsby', 'F. Scott Fitzgerald', '1925', 180);
