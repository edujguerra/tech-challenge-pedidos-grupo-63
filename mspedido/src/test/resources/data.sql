--DROP TABLE IF EXISTS item_pedido;
--DROP TABLE IF EXISTS pedido;
--
--CREATE TABLE IF NOT EXISTS pedido (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--       nome_cliente VARCHAR(255),
--       valor_total DECIMAL(10, 2),
--       status ENUM('PEDIDO_RECEBIDO', 'AGUARDANDO_PAGAMENTO', 'PEDIDO_PAGO', 'PREPARANDO_PARA_ENVIO', 'EM_TRANSITO', 'ENTREGUE'),
--       entregador_id INT
--);
--
--CREATE TABLE IF NOT EXISTS item_pedido (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    id_pedido INT,
--    id_produto INT,
--    quantidade INT,
--    FOREIGN KEY (id_pedido) REFERENCES pedido(id)
--);

-- Exemplo 1
INSERT INTO pedido (nome_cliente, valor_total, status, entregador_id)
VALUES ('João Silva', 75.50, 'PEDIDO_RECEBIDO', 101);
INSERT INTO item_pedido (id_pedido, id_produto, quantidade)
VALUES (1, 1, 2);

-- Exemplo 2
INSERT INTO pedido (nome_cliente, valor_total, status, entregador_id)
VALUES ('Maria Oliveira', 120.25, 'AGUARDANDO_PAGAMENTO', 102);
INSERT INTO item_pedido (id_pedido, id_produto, quantidade)
VALUES (2, 2, 3);