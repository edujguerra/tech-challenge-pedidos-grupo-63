package br.com.fiap.model.dtos;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProdutoDTOTest {

    @Test
    public void testNoArgsConstructor() {
        ProdutoDTO produto = new ProdutoDTO();
        assertThat(produto).isNotNull();
    }

    @Test
    public void testAllArgsConstructor() {
        ProdutoDTO produto = new ProdutoDTO(1, "Produto1", "Descricao do Produto1", 100, 10.0);
        assertThat(produto.getId()).isEqualTo(1);
        assertThat(produto.getNome()).isEqualTo("Produto1");
        assertThat(produto.getDescricao()).isEqualTo("Descricao do Produto1");
        assertThat(produto.getQuantidade_estoque()).isEqualTo(100);
        assertThat(produto.getPreco()).isEqualTo(10.0);
    }

    @Test
    public void testSettersAndGetters() {
        ProdutoDTO produto = new ProdutoDTO();
        produto.setId(2);
        produto.setNome("Produto2");
        produto.setDescricao("Descricao do Produto2");
        produto.setQuantidade_estoque(200);
        produto.setPreco(20.0);

        assertThat(produto.getId()).isEqualTo(2);
        assertThat(produto.getNome()).isEqualTo("Produto2");
        assertThat(produto.getDescricao()).isEqualTo("Descricao do Produto2");
        assertThat(produto.getQuantidade_estoque()).isEqualTo(200);
        assertThat(produto.getPreco()).isEqualTo(20.0);
    }

    @Test
    public void testEqualsAndHashCode() {
        ProdutoDTO produto1 = new ProdutoDTO(1, "Produto1", "Descricao do Produto1", 100, 10.0);
        ProdutoDTO produto2 = new ProdutoDTO(1, "Produto1", "Descricao do Produto1", 100, 10.0);
        ProdutoDTO produto3 = new ProdutoDTO(2, "Produto2", "Descricao do Produto2", 200, 20.0);

        assertThat(produto1).isEqualTo(produto2);
        assertThat(produto1).hasSameHashCodeAs(produto2);
        assertThat(produto1).isNotEqualTo(produto3);
        assertThat(produto1.hashCode()).isNotEqualTo(produto3.hashCode());
    }
}
