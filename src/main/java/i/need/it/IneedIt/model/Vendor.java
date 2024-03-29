package i.need.it.IneedIt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vendor", schema = "ineedit")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VENDOR_ID")
    private long vendorId;

    @Column(name = "VENDOR_NAME")
    private String vendorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEEDING_EVENT_ID")
    private NeedingEvent needingEvent;

//    @ManyToOne
//    @JoinColumn(name = "VEND")
//    private Vendor vendor;
}
