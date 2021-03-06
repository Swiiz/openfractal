/*
GLSL-Complex-Numbers
Library that adds support for GLSL to handle complex numbers uses vec2 as the base type all functions are their normal real name prefixed with 'cpx_'.
https://github.com/Quinn-With-Two-Ns/GLSL-Complex-Numbers
*/
precision highp float;

#define M_PI 3.1415926535897932384626433832795
// Define the imaginary unit, didn't use the standard i or j cause they areoften used in for loops
#define I vec2(0,1)

vec2 cpx_con(in vec2 z) {
    return vec2(z.x, -z.y);
}

vec2 cpx_mul(in vec2 z, in vec2 w) {
    return vec2(z.x * w.x - z.y * w.y, z.y * w.x + z.x * w.y);
}

float cpx_mag2(in vec2 z) {
    return dot(z,z);
}

float cpx_mag(in vec2 z) {
    return sqrt(dot(z,z));
}

vec2 cpx_div(in vec2 z, in vec2 w) {
    return cpx_mul(z, cpx_con(w))/cpx_mag2(w);
}

float cpx_arg(in vec2 z){
    float r = atan(z.y,z.x);
    return r;
}

vec2 cpx_exp(in vec2 z){
    vec2 w = vec2( cos(z.y), sin(z.y));
    w *= exp(z.x);
    return w;
}

vec2 cpx_log(in vec2 z){
    return vec2( log(cpx_mag(z)), cpx_arg(z));
}

vec2 cpx_pow(in vec2 z, float a) {
    return cpx_exp(cpx_log(z) * a );
}

vec2 cpx_sqrt(in vec2 z){
    return cpx_pow(z, 0.5);
}
// Trig Functions
vec2 cpx_cos(in vec2 z){
    vec2 w = cpx_exp( cpx_mul( z, I) ) + cpx_exp( cpx_mul( z, -1.0*I) );
    return 0.5 * w;
}

vec2 cpx_sin(in vec2 z){
    vec2 w = cpx_exp( cpx_mul( z, I) ) - cpx_exp( cpx_mul( z, -1.0*I) );
    return 0.5 * cpx_mul( w, -1.0*I );
}

vec2 cpx_tan(in vec2 z){
    vec2 a = cpx_sin(z);
    vec2 b = cpx_cos(z);
    return cpx_div(a,b);
}
// Inverse Trig
vec2 cpx_asin(in vec2 z){
    vec2 w = cpx_log( cpx_mul(I,z) + cpx_sqrt( vec2(1,0) - cpx_pow(z,2.0)) );
    return cpx_mul(-1.0*I,w);
}

vec2 cpx_acos(in vec2 z){
    vec2 w = cpx_log( z + cpx_sqrt( cpx_pow(z,2.0) + vec2(1,0) ) );
    return cpx_mul(-1.0*I,w);
}

vec2 cpx_atan(in vec2 z){
    vec2 w = cpx_log( vec2(1,0) - cpx_mul(z,I)) - cpx_log( vec2(1,0) + cpx_mul(z,I));
    return cpx_mul(0.5*I,w);
}
// Hyperbolic Trig
vec2 cpx_cosh(in vec2 z){
    vec2 w = cpx_exp( z ) + cpx_exp( -1.0*z );;
    return 0.5 * w;
}

vec2 cpx_sinh(in vec2 z){
    vec2 w = cpx_exp( z ) - cpx_exp( -1.0*z );
    return 0.5 * cpx_mul( w, -1.0*I );
}

vec2 cpx_tanh(in vec2 z){
    vec2 a = cpx_sinh(z);
    vec2 b = cpx_cosh(z);
    return cpx_div(a,b);
}

// Extra fun things
vec2 collatz_map(in vec2 z){
    return 0.25*( vec2(1,0) + 4.0*z - cpx_mul( 1.0+2.0*z, cpx_cos(M_PI*z)) );
}
// Simple Domain Colouring
vec3 cpx2hsv(in vec2 z){
    return vec3(cpx_arg(z)/(2.0*M_PI), 1.0, 1.0-exp(-1.0*cpx_mag(z)) );
}

float hue2rgb(float f1, float f2, float hue) {
    if (hue < 0.0)
    hue += 1.0;
    else if (hue > 1.0)
    hue -= 1.0;
    float res;
    if ((6.0 * hue) < 1.0)
    res = f1 + (f2 - f1) * 6.0 * hue;
    else if ((2.0 * hue) < 1.0)
    res = f2;
    else if ((3.0 * hue) < 2.0)
    res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;
    else
    res = f1;
    return res;
}

vec3 hsl2rgb(vec3 hsl) {
    vec3 rgb;

    if (hsl.y == 0.0) {
        rgb = vec3(hsl.z); // Luminance
    } else {
        float f2;

        if (hsl.z < 0.5)
        f2 = hsl.z * (1.0 + hsl.y);
        else
        f2 = hsl.z + hsl.y - hsl.y * hsl.z;

        float f1 = 2.0 * hsl.z - f2;

        rgb.r = hue2rgb(f1, f2, hsl.x + (1.0/3.0));
        rgb.g = hue2rgb(f1, f2, hsl.x);
        rgb.b = hue2rgb(f1, f2, hsl.x - (1.0/3.0));
    }
    return rgb;
}
// END OF LIBS

vec2 of_mapToComplex(vec2 dims, vec2 coords, vec2 offset) {
    return ((coords / dims.y) + offset - vec2(0.5*(dims.x/dims.y + offset.x), .5));
}

// Variables

uniform vec2 of_dimensions; //vec2(width, height)
uniform vec2 of_cursorPos;
uniform vec2 of_cursorPosFromCenter;
uniform vec2 of_position;
uniform float of_zoom;
uniform float of_time;

out vec4 fragColor;

/*
Real Shader will be injected just below
*/