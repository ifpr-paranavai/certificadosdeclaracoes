package br.com.cedi.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AtividadeComplementarService implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<String> getListaAtividadesComplementaresG1() {
    List<String> listaAtividadesComplementares = new ArrayList<>();
    listaAtividadesComplementares.add("G1-Estágio Supervisionado (Não Obrigatório)");
    listaAtividadesComplementares.add("G1-Monitoria");
    listaAtividadesComplementares.add("G1-Aula magna");
    listaAtividadesComplementares.add("G1-Palestras em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Fóruns e seminários em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Conferências e Congressos em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Debates em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Encontros em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Jornadas acadêmicas");
    listaAtividadesComplementares.add("G1-Simpósios em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Visitas monitoradas realizas pelo IFPR");
    listaAtividadesComplementares.add("G1-Atividades de campo");
    listaAtividadesComplementares.add("G1-Outros cursos técnicos ou de graduação em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Curso de qualificação em áreas correlatas ao curso - EAD");
    listaAtividadesComplementares.add("G1-Curso de qualificação em áreas correlatas ao curso - Semipresencial");
    listaAtividadesComplementares.add("G1-Curso de qualificação em áreas correlatas ao curso - Presencial");
    listaAtividadesComplementares.add("G1-Participação em projetos de ensino em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Participação em grupos de estudos em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Olimpíadas do conhecimento em áreas correlatas ao curso");
    listaAtividadesComplementares.add("G1-Outras Atividades de Ensino");
    return listaAtividadesComplementares;
  }

  private List<String> getListaAtividadesComplementaresG2() {
    List<String> listaAtividadesComplementares = new ArrayList<>();
    listaAtividadesComplementares.add("G2-Participação em programas de bolsas Institucionais");
    listaAtividadesComplementares.add("G2-Participação em programas de bolsas ofertadas por Agências de Fomento");
    listaAtividadesComplementares.add(
        "G2-Participação em Projetos de Iniciação Científicas, relacionados com objetivo do curso");
    listaAtividadesComplementares.add("G2-Participação como colaborador em projetos de Pesquisa, Extensão e Inovação");
    listaAtividadesComplementares.add(
        "G2-Participação como apresentador de trabalhos em palestras, congressos, seminários e mini cursos");
    listaAtividadesComplementares.add("G2-Participação como expositor em exposições técnico-científicas");
    listaAtividadesComplementares.add(
        "G2-Participação efetiva na organização de exposições e seminários de caráter acadêmico");
    listaAtividadesComplementares.add("G2-Publicações em revistas técnicas");
    listaAtividadesComplementares.add(
        "G2-Publicações em anais de eventos técnico-científicos ou em periódicos científicos de abrangência local, "
            + "regional, nacional ou internacional");
    listaAtividadesComplementares.add("G2-Livro ou capítulo de livros publicados");
    listaAtividadesComplementares.add("G2-Participação em grupos de pesquisa");
    listaAtividadesComplementares.add("G2-Participação em Empresa Junior, Hotel Tecnológico, Incubadora Tecnológica");
    listaAtividadesComplementares.add("G2-Participação em projetos multidisciplinares ou interdisciplinares");
    listaAtividadesComplementares.add("G2-Outras Atividades de Pesquisa, Extensão e Inovação");
    return listaAtividadesComplementares;
  }

  private List<String> getListaAtividadesComplementaresG3() {
    List<String> listaAtividadesComplementares = new ArrayList<>();
    listaAtividadesComplementares.add("G3-Participação em atividades esportivas");
    listaAtividadesComplementares.add("G3-Participação em cursos de línguas estrangeiras");
    listaAtividadesComplementares.add(
        "G3-Participação em atividades artísticas e culturais, tais como: banda marcial, camerata de sopro, teatro, coral, radioamadorismo e outras");
    listaAtividadesComplementares.add(
        "G3-Participação efetiva na organização de exposições e seminários de caráter artísticos ou cultural");
    listaAtividadesComplementares.add("G3-Participação como expositor em exposição artística ou cultural");
    listaAtividadesComplementares.add(
        "G3-Participação efetiva em Diretórios e Centros Acadêmicos, Entidades de Classe e Colegiados internos à Instituição");
    listaAtividadesComplementares.add(
        "G3-Participação efetiva em trabalho voluntário, atividades comunitárias, CIPAS, associações de bairros, brigadas de incêndio e associações escolares");
    listaAtividadesComplementares.add("G3-Participação em atividades beneficentes");
    listaAtividadesComplementares.add(
        "G3-Atuação como instrutor em palestras técnicas, seminários, cursos da área específica, desde que não remunerados");
    listaAtividadesComplementares
        .add("G3-Engajamento como docente não remunerado em cursos preparatórios e de reforço escolar");
    listaAtividadesComplementares
        .add("G3-Participação em projetos de extensão, não remunerados, e de interesse social");
    listaAtividadesComplementares.add("G3-Serviço eleitoral obrigatório");
    listaAtividadesComplementares.add("G3-Outras Atividades de Formação Social, Humana e Cultural");
    return listaAtividadesComplementares;
  }

  public List<String> getListaAtividadesComplementares() {
    List<String> listaAtividadesComplementares = new ArrayList<>();
    listaAtividadesComplementares.addAll(getListaAtividadesComplementaresG1());
    listaAtividadesComplementares.addAll(getListaAtividadesComplementaresG2());
    listaAtividadesComplementares.addAll(getListaAtividadesComplementaresG3());
    return listaAtividadesComplementares;
  }

}
