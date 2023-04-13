package product;


import com.titashop.category.CategoryRepository;
import com.titashop.common.entity.Product;
import com.titashop.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByAlias(){
        String alias = "canon-eos-m50";
        Product product = productRepository.findByAlias(alias);

        assertThat(product).isNotNull();
    }

}
