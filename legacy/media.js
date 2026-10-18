// Calcule a media das notas dadas em uma lista chamada notas.
// A lista deve conter 5 notas.
// Exiba a media no console.

var notas = [10, 8, 7, 9, 5];
var soma = 0;
var media = 0;

for (var i = 0; i < notas.length; i++) {
    soma += notas[i];
}

media = soma / notas.length;

console.log(media);


// Path: teste.js