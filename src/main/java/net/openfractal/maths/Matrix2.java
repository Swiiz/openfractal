package net.openfractal.maths;

public class Matrix2 {
    private float m_00, m_01,
                  m_10, m_11;

    public Matrix2(float m_00, float m_01, float m_10, float m_11) {
        this.m_00 = m_00;
        this.m_01 = m_01;
        this.m_10 = m_10;
        this.m_11 = m_11;
    }

    public float[] asArray() {
        return new float[] { m_00, m_01, m_10, m_11 };
    }
}
