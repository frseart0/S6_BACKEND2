package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void save_persisteVentaConDetalleDeProductos() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Agua");
        producto.setPrecio(500.0);
        producto.setStock(10);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecio(500.0);

        Venta venta = new Venta();
        venta.setFecha(new Date());
        venta.setDetalles(List.of(detalle));

        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.save(venta);

        ArgumentCaptor<Venta> captor = ArgumentCaptor.forClass(Venta.class);
        verify(ventaRepository).save(captor.capture());

        DetalleVenta detalleGuardado = captor.getValue().getDetalles().get(0);
        assertEquals(2, detalleGuardado.getCantidad());
        assertEquals(500.0, detalleGuardado.getPrecio());
        assertEquals("Agua", detalleGuardado.getProducto().getNombre());
        assertEquals(venta, resultado);
    }
}
