package br.upe.aula.controlepeso.servico;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.upe.aula.controlepeso.entidade.Usuario;
import br.upe.aula.controlepeso.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario incluir(Usuario usuario) {
        // aplicar regras de negocio

        // 1. Preenchimento de campos obrigatórios
        if(!areFieldsFilled(usuario)) {
            throw new RuntimeException("Preencha os campos");
        }
        // 2. Verificar validade email
        if(!isEmailValid(usuario)) {
            throw new RuntimeException("Informe um e-mail válido");
        }
        // 3. Validar se existe email já cadastrado
        if(emailExists(usuario)) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        // 4. A Altura deve ser maior do que 100 camada
        if(!isHeightValid(usuario)) {
            throw new RuntimeException("A altura deve ser maior que 100");
        }
        // 5. Peso inicial deve ser maior do que 30kg
        if(!isInitialWeightValid(usuario)) {
            throw new RuntimeException("O peso inicial deve ser maior que 30kg");
        }
        // 6. Verificar se a data do peso objetivo deve ser no mínimo de uma semana

        usuario.setId(null);
        usuario.setDataInicial(LocalDate.now());

        // delegar a camada de dados para salvar no banco de dados
        return usuarioRepositorio.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

    public void excluir(Long id) {

        if (id == null || id == 0l) {
            throw new RuntimeException("Informe um identificador de usuário");
        }

        if (!this.usuarioRepositorio.existsById(id)) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador:" + id);
        }

        this.usuarioRepositorio.deleteById(id);
    }

    public Usuario alterar(Usuario usuario) {
        // aplicar regras de negocio

        // 0. verificar se existe
        if (usuario == null) {
            throw new RuntimeException("Informe os dados usuário");
        }

        if (usuario.getId() == null || usuario.getId() == 0l) {
            throw new RuntimeException("Informe um identificador de usuário");
        }

        if (!this.usuarioRepositorio.existsById(usuario.getId())) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador:" + usuario.getId());
        }

        // 1. Preenchimento de campos obrigatórios
        if(!areFieldsFilled(usuario)) {
            throw new RuntimeException("Preencha os campos");
        }
        // 2. Verificar validade email
        if(!isEmailValid(usuario)) {
            throw new RuntimeException("Informe um e-mail válido");
        }
        // 3. Validar se existe email já cadastrado
        if(emailExists(usuario)) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        // 4. A Altura deve ser maior do que 100 camada
        if(!isHeightValid(usuario)) {
            throw new RuntimeException("A altura deve ser maior que 100");
        }
        // 5. Peso inicial deve ser maior do que 30kg
        if(!isInitialWeightValid(usuario)) {
            throw new RuntimeException("O peso inicial deve ser maior que 30kg");
        }
        // 6. Verificar se a data do peso objetivo deve ser no mínimo de uma semana

        return usuarioRepositorio.save(usuario);
    }
    private boolean isEmailValid(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean emailExists(Usuario usuario) {
        boolean exists = false;
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        for(Usuario u: usuarios) {
            if(usuario.getEmail() == u.getEmail()) {
                exists = true;
            }
        }
        return exists;
    }

    private boolean isHeightValid(Usuario usuario) {
        if(usuario.getAltura() <= 100) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isInitialWeightValid(Usuario usuario) {
        if(usuario.getPesoInicial() <= 30) {
            return false;
        } else {
            return true;
        }
    }

    private boolean areFieldsFilled(Usuario usuario) {
        if(usuario.getNome().isEmpty()
                && usuario.getEmail().isEmpty()
                && (usuario.getAltura() == null)
                && (usuario.getGenero() == null) && usuario.getPesoInicial() == null
                && usuario.getPesoDesejado() == null && usuario.getDataInicial() == null
                && usuario.getDataObjetivo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
