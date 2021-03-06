# š OpenFractal

āØ A fully open-source software to generate entirely customisable fractals!

[![Apache license](https://img.shields.io/badge/License-Apache-blue.svg)](http://www.apache.org/licenses/)

![Mandelbrot](https://imgur.com/01xq7U8.png) <br />
***Mandelbrot** fractal generated by OpenFractal*

***

### How to make my own Fractal?
**We use OpenGL shaders to generate the fractals.**<br />
You will be able from a configuration file to inject your own code into the shader in order to make your custom fractal. In this file you will also be able to change other options such as the colors... <br />

**Complex numbers**<br />
As you maybe know fractals such as the Mandelbrot set are generated using complex numbers.
> The Mandelbrot set is the set of values of c in the [complex plane](https://en.wikipedia.org/wiki/Complex_plane) for which the orbit of the [critical point](https://en.wikipedia.org/wiki/Complex_quadratic_polynomial#Critical_point) z = 0 under [iteration](https://en.wikipedia.org/wiki/Iterated_function) of the [quadratic map](https://en.wikipedia.org/wiki/Quadratic_map) remains [bounded](https://en.wikipedia.org/wiki/Bounded_sequence). Thus, a [complex number](https://en.wikipedia.org/wiki/Complex_number) c is a member of the Mandelbrot set if, when starting with zā = 0 and applying the iteration repeatedly, the [absolute value](https://en.wikipedia.org/wiki/Absolute_value) of zā remains bounded for all n > 0. <br /><br />
> ![Equation](https://imgur.com/GGmzKmE.png)

To represent them inside our glsl shader, we use 2d vectors:

<img src="https://imgur.com/iFtPC7R.png" width="200">

```GLSL
c = vec2(a, b);
```

We also provide a lot of functions for manipulating complex numbers. See 
[GLSL-Complex-Numbers](https://github.com/Quinn-With-Two-Ns/GLSL-Complex-Numbers).

### Examples fractal code:  (š„ This might not work in the close future)

```GLSL
/*
Open-Mandelbrot
MADE BY SWIIZ FOR OPEN-FRACTAL
*/

const vec4 colorPalette = -vec4(0,23,21,0); // Can be randomly generated for more fun!
const vec4 finalColor = vec4(.0); // Color applied to points inside the set
const int hueModulus = 30; // Allow color cycling based on interactions count

void main() {
    float zoom = .2; // Default zoom
    int recursion = 500, // Default recursion
    i = 0; // Iteration count
    vec2 c = (of_mapToComplex(of_dimensions, gl_FragCoord.xy, of_position)) / of_zoom, // Mapping the complex plane to the viewport
    z = c;
    for(; i <= recursion && cpx_mag(z) <= 2. ; i++ ) z = cpx_pow(z, 2.) + c; // Actual iterative thing
    fragColor =  i - 1 == recursion ? finalColor : .6 + .6 * cos( 6.3 *  (float((i - 1) % hueModulus) / float(hueModulus)) + colorPalette); // Deciding the final color
}
```

```GLSL
/*
Open-Julia
MADE BY SWIIZ FOR OPEN-FRACTAL
*/

const vec2 c = vec2(0.37, .1); // Complex number that will generate the julia set.

const vec4 colorPalette = -vec4(0,23,21,0); // Can be randomly generated for more fun!
const vec4 finalColor = vec4(.0); // Color applied to bounded values
const int hueModulus = 30; // Allow color cycling based on interactions count

void main() {
    int recursion = 1000, // Default recursion
    i = 0; // Iteration count
    vec2 z = (of_mapToComplex(of_dimensions, gl_FragCoord.xy, of_position)) / of_zoom; // Mapping the complex plane to the viewport
    for(; i <= recursion && cpx_mag(z) <= 2. ; i++ ) z = cpx_pow(z, 2.) + c; // Actual iterative thing
    fragColor =  i - 1 == recursion ? finalColor : .6 + .6 * cos( 6.3 *  (float((i - 1) % hueModulus) / float(hueModulus)) + colorPalette); // Deciding the final color
}
```

***

![Evolving Mandelbrot](https://imgur.com/eyuWAXF.png)
*"**Quadratic** map **Mandelbrot**" fractal generated by OpenFractal*

***

### Plans
**Sharing fractals online?**<br />
We also plan to create a website where you could share your fractals, and you would have a desktop application from where you would be able to create fractals or download fractals from internet.
<br />

**3D fractals?**<br />
It is possible that we later add three dimensional fractals (or maybe even more dimensions).

**API?**<br />
We will maybe add a java api that will allow you to mess up with the software at runtime, or maybe we will add "addons".

***

![Julia Set](https://imgur.com/zkUWWia.png)
*(0.37 + 0.1i) **Julia set** generated by Open-Fractal*

***

### ā Todo:
- Create documentation for the new glsl functions and varible.
- Shader error tracking
- Loading config file
- Waiting for changes on the config file
- Clean code
- **(Not for now)** *Make the OpenFractal website and client*

***

### š Feel free to help on the project as this is pure opensource!

