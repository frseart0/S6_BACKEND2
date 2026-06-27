package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void save_registraMovimientoEntradaConProductoAsociado() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        producto.setPrecio(1200.0);
        producto.setStock(50);

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(10);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());

        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.save(inventario);

        ArgumentCaptor<Inventario> captor = ArgumentCaptor.forClass(Inventario.class);
        verify(inventarioRepository).save(captor.capture());

        Inventario guardado = captor.getValue();
        assertEquals("Entrada", guardado.getTipoMovimiento());
        assertEquals(10, guardado.getCantidad());
        assertEquals(1L, guardado.getProducto().getId());
        assertEquals(inventario, resultado);
    }

    @Test
    void save_registraMovimientoSalidaConProductoAsociado() {
        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Leche");
        producto.setPrecio(900.0);
        producto.setStock(30);

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(3);
        inventario.setTipoMovimiento("Salida");
        inventario.setFechaMovimiento(new Date());

        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        inventarioService.save(inventario);

        ArgumentCaptor<Inventario> captor = ArgumentCaptor.forClass(Inventario.class);
        verify(inventarioRepository).save(captor.capture());
        assertEquals("Salida", captor.getValue().getTipoMovimiento());
        assertEquals(2L, captor.getValue().getProducto().getId());
    }
}
