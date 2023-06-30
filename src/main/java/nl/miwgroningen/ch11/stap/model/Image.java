package nl.miwgroningen.ch11.stap.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Tristan Meinsma
 * This is an image for a course
 */
@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue
    private Long imageId;

    @Column(nullable = false)
    private String imageName;

    @Lob
    private byte[] encodedImage;

    public String getImagePath() {
      return "/photos" + imageName;
    }

}
