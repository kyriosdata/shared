# HealthDB
Health Database Management System 

[<img src="https://api.travis-ci.org/kyriosdata/regras.svg?branch=master">](https://travis-ci.org/kyriosdata/regras)
[![Dependency Status](https://www.versioneye.com/user/projects/5818f81589f0a91d55eb921c/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5818f81589f0a91d55eb921c)
[![Sonarqube](https://sonarqube.com/api/badges/gate?key=com.github.kyriosdata.regras:regras)](https://sonarqube.com/dashboard/index?id=com.github.kyriosdata.regras%3Aregras)
[![Javadocs](http://javadoc.io/badge/com.github.kyriosdata.regras/regras.svg)](http://javadoc.io/doc/com.github.kyriosdata.regras/regras)

<br />
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/">
<img alt="Creative Commons License" style="border-width:0"
 src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a>
 <br />This work is licensed under a <a rel="license" 
 href="http://creativecommons.org/licenses/by/4.0/">Creative Commons 
 Attribution 4.0 International License</a>. 
 <br />Fábio Nogueira de Lucena - Fábrica de Software - 
 Instituto de Informática (UFG).

## Como usar (via maven)?

Acrescente a dependência abaixo no arquivo pom.xml:

<pre>
&lt;dependency&gt;
  &lt;groupId&gt;com.github.kyriosdata.ringBuffer&lt;/groupId&gt;
  &lt;artifactId&gt;ringBuffer&lt;/artifactId&gt;
  &lt;version&gt;1.0.0&lt;/version&gt;
&lt;/dependency&gt;
</pre>

## O que essa biblioteca oferece?
Implementação de mecanismo de controle de concorrência, baseada 
em buffer circular, com ênfase na simplicidade e desempenho. 
Isso significa eliminar o uso de bibliotecas que oferecem 
acesso concorrente ou mesmo de alternativas como LMAX Disrupter.