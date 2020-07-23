package org.cinema.Controllers;

import lombok.Data;
import org.cinema.entities.Cinema;
import org.cinema.entities.Film;
import org.cinema.entities.Ticket;
import org.cinema.repositories.FilmRepository;
import org.cinema.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.cinema.repositories.CinemaRepository;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
    /*@Autowired
    private CinemaRepository cinemaRepository;
    @GetMapping("/cinemas")
    public List<Cinema>cinemas(){
        return cinemaRepository.findAll();
    }
    @GetMapping("/cinemas/{id}")
    public Cinema cinema(@PathVariable Long id){
        return cinemaRepository.findById(id).get();
    }
    @PostMapping("/cinemas")
    public Cinema save(@RequestBody Cinema cinema){
        return cinemaRepository.save(cinema);
    }
    @DeleteMapping("/cinemas/{id}")
    public void delete(@PathVariable Long id){
        cinemaRepository.deleteById(id);
    }
    @PutMapping("/cinemas/{id}")
    public Cinema update(@RequestBody Cinema cinema,@PathVariable Long id){
        cinema.setId(id);
        return cinemaRepository.save(cinema);
    }
    @Autowired
    private FilmRepository filmRepository;
    @GetMapping("/ListFilms")
    public List<Film> films(){
        return filmRepository.findAll();
    }*/
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @GetMapping(path = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] images(@PathVariable(name = "id") Long id) throws Exception{
        Film film=filmRepository.findById(id).get();
        String photoName=film.getPhoto();
        File file=new File(System.getProperty("user.home")+"/Cinemas_Pictures/"+photoName);
        Path path= Paths.get(file.toURI());
        return Files.readAllBytes(path);

    }
    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
        List<Ticket>ticketList=new ArrayList<>();
        ticketForm.getTickets().forEach(idTicket->{
            Ticket ticket=ticketRepository.findById(idTicket).get();
            ticket.setName_client(ticketForm.getClientName());
            ticket.setReserve(true);
            ticket.setCode_payment(ticketForm.getCodePayement());
            ticketRepository.save(ticket);
            ticketList.add(ticket);

        });
        return ticketList;
    }
}
@Data
class TicketForm{
    private String ClientName;
    private int codePayement;
    private List<Long> tickets=new ArrayList<>();
}
