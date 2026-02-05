import './VerticalProgressBar.css'

export interface BarProps {
    max : number, done : number,
    date : string, height : number,
    width : number, barColor : string,
    backgroundColor : string
}

export default function VerticalProgressBar(props : BarProps) {
    const { max, done, date, height, width, barColor, backgroundColor } = props;

    const percentage = max > 0 ? Math.min(100, (done / max) * 100) : 0;

    const filledHeight = (percentage / 100) * height;

    return (
        <div className="vertical-progress-container" style={{ width: width + 40 }}>
            <div className="vertical-progress-wrapper responsive-bar">
                <div
                    className="progress-background"
                    style={{
                        height: `${height}px`,
                        minWidth: `${width}px`,
                        backgroundColor: backgroundColor,
                        borderWidth: `1px`,
                        borderColor: `black`
                    }}>
                    <p className="progress-text">{done}</p>
                    <div
                        className="progress-filled"
                        style={{
                            height: `${filledHeight}px`,
                            minWidth: `${width}px`,
                            backgroundColor: barColor,
                            bottom: 0
                        }}
                    ></div>
                </div>
            </div>

            <div className="progress-date">
                <b>{date}</b>
            </div>

            <div className="progress-values">
                <span>{done}</span> / <span>{max}</span>
            </div>
        </div>
    );
};