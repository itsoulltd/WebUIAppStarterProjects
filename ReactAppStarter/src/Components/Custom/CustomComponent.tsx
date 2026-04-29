import React from 'react'

interface Props {
    children?: string;
    onClick?: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

function CustomComponent({children, onClick}: Props) {
    //TODO:
    return (
        <>
            <button onClick={onClick}>{children}</button>
        </>
    );
}

export default CustomComponent;