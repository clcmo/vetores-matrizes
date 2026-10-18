# Matrizes e Vetores — app Android para aprender fazendo

[![GitHub license](https://img.shields.io/github/license/clcmo/gitteste?style=for-the-badge)](https://github.com/clcmo/gitteste)
[![GitHub stars](https://img.shields.io/github/stars/clcmo/gitteste?style=for-the-badge)](https://github.com/clcmo/gitteste/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/clcmo/gitteste?style=for-the-badge)](https://github.com/clcmo/gitteste/network)
[![GitHub issues](https://img.shields.io/github/issues/clcmo/gitteste?style=for-the-badge)](https://github.com/clcmo/gitteste/issues)

App Android (Kotlin + Jetpack Compose, arquitetura MVVM) que ensina vetores e matrizes com **passo a passo** em cada
operação.

## Sequência didática sugerida

1. **Vetores como setas** — mexa nos controles e veja a seta se mover no plano.
2. **Soma, subtração e escalar** — regra do paralelogramo e "esticar/encolher/inverter".
3. **Módulo e produto escalar** — comprimento, perpendicularidade e ângulo.
4. **Matrizes como tabelas** — soma, subtração, escalar e transposta, elemento a elemento.
5. **Multiplicação de matrizes** — linha × coluna, com a regra dos tamanhos.
6. **Exercícios** — o aluno calcula, o app corrige e mostra o passo a passo.

## Estrutura

```
app/src/main/java/com/clcmo/gitteste/
├── domain/   # Matrix, Vec, MatrixOps, VectorOps — Kotlin puro, testável sem Android
├── ui/       # ViewModels (estado) + telas Compose
└── MainActivity.kt
legacy/       # arquivos originais (matrizes.java, media.js)
```

## Como rodar

1. Abra a pasta no Android Studio (ele gera o Gradle Wrapper e baixa o SDK).
2. Rode os testes: `./gradlew :app:testDebugUnitTest`
3. Execute no emulador ou celular.

Licença: [MIT](../LICENCE)
