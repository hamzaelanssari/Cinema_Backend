package org.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="ticketProj",types= {org.cinema.entities.Ticket.class})
public interface TicketProjection{
    public Long getId();
    public String getName_client();
    public double getPrix();
    public Integer getCode_payment();
    public boolean isReserve();
    public Place getPlace();
}