/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Servicios.HogarExpert.Controller;

import com.Servicios.HogarExpert.Exception.MiException;
import com.Servicios.HogarExpert.Entity.Usuario;
import com.Servicios.HogarExpert.Service.IUsuarioServicio;
import java.util.List;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController()
@RequestMapping("/usuario")
@CrossOrigin("*")

public class UsuarioControlador {
    
    @Autowired
    private IUsuarioServicio usuarioServi;
    

 
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DemoRest.class);
    
       @PostMapping("/crear")
    public ResponseEntity<Usuario> crearUsuario(@ModelAttribute Usuario usuario, @RequestParam("archivo")  MultipartFile archivo) {
        try {
            Usuario u = Usuario.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .username(usuario.getUsername())
                    .celular(usuario.getCelular())
                    .dni(usuario.getDni())
                    .domicilio(usuario.getDomicilio())
                    .email(usuario.getEmail())
                    .password( new BCryptPasswordEncoder().encode(usuario.getPassword()) )
                    .roles(usuario.getRoles())
                    .localidad(usuario.getLocalidad().toUpperCase())
                    
                    .build();         
                usuarioServi.save(u,archivo);
 
            System.out.println("usuario creado");
                return ResponseEntity.status(HttpStatus.OK).body(u) ;
       
            
        } catch (MiException ex) {
          System.out.println(ex.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) ;
         }
            
            
        
         
    } 
    
    @GetMapping("/perfil/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Usuario verUsuario(@PathVariable Long id){
        try {
            Usuario u = usuarioServi.findById(id);
            
            return u;
        } catch (MiException ex) {
            System.out.println(ex.getMessage());
            return null;
            
        }
     
        }
    
    @GetMapping("/lista")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Usuario>listaUsuarios(){
      try {
             logger.info("Accediendo a la lista de usuarios...");
      
            List<Usuario> usuarios = usuarioServi.findAll();
            // Agrega logs para verificar si la lista de usuarios se está obteniendo correctamente
            logger.info("Usuarios obtenidos correctamente: {}", usuarios);
            return usuarios;
        } catch (Exception e) {
            // Agrega logs para capturar cualquier excepción que pueda ocurrir
            logger.error("Error al obtener la lista de usuarios", e);
            throw e;
        }
    }
    
    
    
    @PutMapping("/modificar/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Usuario> modificarUsuario( @ModelAttribute Usuario usuario ) throws MiException{
     //, @RequestParam("archivo") MultipartFile archivo 
        try{
         
       Usuario u =  usuarioServi.update( usuario);//,archivo
      
            System.out.println( "exito usuario modificado");
            return ResponseEntity.ok(u);
        }catch(MiException ex){
            
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        
    }
    
    @PutMapping("/modificarImg/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Usuario>modificarUsuarioImg(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo){
        try{
        Usuario u = usuarioServi.updateImg(id, archivo);
            System.out.println("img modificada");
        return ResponseEntity.ok(u);
        } catch(Exception e){
            System.out.println(e.getMessage());
        return ResponseEntity.badRequest().body(null);
        }
        
    }
    
    
    
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public void eliminarUsuario(@PathVariable Long id){
        try {
            usuarioServi.delete(id);
            System.out.println("usuario eliminado");
        } catch (MiException ex) {
            System.out.println(ex.getMessage());
        }
        
        
    }
    
}
