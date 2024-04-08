package i.need.it.IneedIt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "vendor", schema = "ineedit")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VENDOR_ID")
    private long vendorId;

    @Column(name = "vendor_name")
    private String vendorName;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private List<NeedingEvent> vendorItems;




}
