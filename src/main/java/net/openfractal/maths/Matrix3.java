package net.openfractal.maths;

public class Matrix3 {
    private float m_00, m_01, m_02,
                  m_10, m_11, m_12,
                  m_20, m_21, m_22;

    public Matrix3(float m_00, float m_01, float m_02, float m_10, float m_11, float m_12, float m_20, float m_21, float m_22) {
        this.m_00 = m_00;
        this.m_01 = m_01;
        this.m_02 = m_02;
        this.m_10 = m_10;
        this.m_11 = m_11;
        this.m_12 = m_12;
        this.m_20 = m_20;
        this.m_21 = m_21;
        this.m_22 = m_22;
    }

    public float[] asArray() {
        return new float[] {
                m_00, m_01, m_02,
                m_10, m_11, m_12,
                m_20, m_21, m_22
        };
    }
}
