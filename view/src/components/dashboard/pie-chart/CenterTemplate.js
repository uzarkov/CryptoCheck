export const CenterTemplate = (pieChart, value) => {
    return (
        <svg>
            <text textAnchor="middle" x="100" y="120" style={{ fontSize: 21, fill: '#494949' }}>
                <tspan x="100"  dy="-10px" style={{ fontWeight: 600 }}>{`$${value}`}</tspan>
            </text>
        </svg>
    );
}