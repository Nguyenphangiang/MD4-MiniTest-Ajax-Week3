package com.bat.controller;

import com.bat.model.Book;
import com.bat.model.BookForm;
import com.bat.model.Category;
import com.bat.service.book.IBookService;
import com.bat.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:upload_file.properties")
@RequestMapping("/books")
public class BookController {
    @Value("${file-upload}")
    private String fileUpload;
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView showList(){
        ModelAndView modelAndView = new ModelAndView("list");
        modelAndView.addObject("books",bookService.findAll());
        modelAndView.addObject("categories",categoryService.findAll());
        return modelAndView;
    }
    @GetMapping
    public ResponseEntity<Iterable<Book>> showAllBook(){
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Book> saveBook(@ModelAttribute BookForm bookForm){
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
//        fileName = System.currentTimeMillis() + fileName;
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book = new Book(bookForm.getName(),bookForm.getPrice(),bookForm.getAuthor(),fileName,bookForm.getCategory());
        bookService.save(book);
        return new ResponseEntity<>(book,HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id){
        Optional<Book> optionalBook = bookService.findById(id);
        if (!optionalBook.isPresent()){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.remove(id);
        return new ResponseEntity<>(optionalBook.get(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> findOne(@PathVariable Long id){
        return new ResponseEntity<>(bookService.findById(id).get(),HttpStatus.OK);
    }
    @PostMapping (path = "/{id}")
    public ResponseEntity<Book> editBook(@PathVariable Long id,@ModelAttribute BookForm bookForm){
        Optional<Book> bookOptional = bookService.findById(id);
        bookForm.setId(bookOptional.get().getId());
        if (!bookOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            MultipartFile file = bookForm.getImage();
            String fileName = file.getOriginalFilename();
            Category category = bookForm.getCategory();
            Book updateBook = new Book(bookForm.getName(),bookForm.getPrice(),bookForm.getAuthor(),fileName,category);
            try{
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (updateBook.getImage().equals("filename.jpg")){
                updateBook.setImage(bookOptional.get().getImage());
            }
            bookService.remove(id);
            bookService.save(updateBook);
            return new ResponseEntity<>(updateBook,HttpStatus.OK);
        }
    }
}
