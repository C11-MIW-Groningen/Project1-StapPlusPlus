package nl.miwgroningen.ch11.stap.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

//    private byte[] encodedImage;

    public String getImagePath() {
      return "/photos" + imageName;
    }

}
