package com.realthomasmiles.marketplace.model.marketplace;

import com.realthomasmiles.marketplace.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "postings")
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private String article;
    @Column(name = "is_active")
    private Boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "location_id")
    private Location location;
    @Temporal(TemporalType.TIMESTAMP)
    @OneToMany(mappedBy = "posting", cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Offer> offers;
    private Date posted;
    private String name;
    private String description;
    private Long price;

}
