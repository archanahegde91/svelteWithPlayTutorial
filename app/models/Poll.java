package models;


import io.ebean.Finder;
import io.ebean.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "poll")
public class Poll extends Model {

    @Id
    @Column(name = "uuid")
    public UUID uuid;

    @Column(name="question")
    public String question;

    @Column(name="answera")
    public String answera;

    @Column(name="answerb")
    public String answerb;

    @Column(name="votesa")
    public int votesa;

    @Column(name="votesb")
    public int votesb;

    protected static Finder<UUID, Poll> find = new Finder<UUID, Poll>(Poll.class);

    public static List<Poll> all(){
        return find.all();
    }

    public static Poll findById(UUID id){
        return find.byId(id);
    }

    public Poll(){
        super();
        this.uuid = UUID.randomUUID();
    }


}
