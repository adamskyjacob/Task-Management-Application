import { CSSProperties } from "react";

export default function Logo(props: { width: number, style?: CSSProperties }) {
    const { width, style } = props;

    const scale = width / 240;

    function scaleUp(dim: number) {
        return dim * scale;
    }


    return (
        <svg xmlns="http://www.w3.org/2000/svg" viewBox={`0 0 ${width} ${width}`} width={width} height={width} style={style}>
            <circle cx={scaleUp(120)} cy={scaleUp(120)} r={scaleUp(90)} fill="#3498db" />
            <rect x={scaleUp(60)} y={scaleUp(60)} width={scaleUp(30)} height={scaleUp(120)} fill="#ffffff" />
            <rect x={scaleUp(100)} y={scaleUp(60)} width={scaleUp(80)} height={scaleUp(20)} fill="#ffffff" />
            <rect x={scaleUp(100)} y={scaleUp(90)} width={scaleUp(60)} height={scaleUp(20)} fill="#ffffff" />
            <rect x={scaleUp(100)} y={scaleUp(120)} width={scaleUp(70)} height={scaleUp(20)} fill="#ffffff" />
            <rect x={scaleUp(100)} y={scaleUp(150)} width={scaleUp(50)} height={scaleUp(20)} fill="#ffffff" />
        </svg>
    );
}