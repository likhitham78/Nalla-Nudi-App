export type Subject = 'Science' | 'Mathematics' | 'Commerce';

export interface Word {
  id: string;
  english: string;
  kannada: string;
  explanation: string;
  example: string;
  subject: Subject;
}

export const words: Word[] = [
  // Science
  {
    id: 's1',
    english: 'Photosynthesis',
    kannada: 'ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ',
    explanation: 'ಸಸ್ಯಗಳು ಸೂರ್ಯನ ಬೆಳಕನ್ನು ಬಳಸಿ ನೀರು ಮತ್ತು ಕಾರ್ಬನ್ ಡೈಆಕ್ಸೈಡ್‌ನಿಂದ ಆಹಾರ ತಯಾರಿಸುವ ಪ್ರಕ್ರಿಯೆ.',
    example: 'ಮರದ ಎಲೆಗಳಲ್ಲಿ ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ ನಡೆಯುತ್ತದೆ.',
    subject: 'Science',
  },
  {
    id: 's2',
    english: 'Evaporation',
    kannada: 'ಆವಿಯಾಗುವಿಕೆ',
    explanation: 'ದ್ರವವು ಬಿಸಿಲಿನಿಂದ ಅಥವಾ ಶಾಖದಿಂದ ಆವಿಯಾಗಿ ಗಾಳಿಯಲ್ಲಿ ಸೇರಿಕೊಳ್ಳುವ ಪ್ರಕ್ರಿಯೆ.',
    example: 'ಬಟ್ಟೆಯ ಮೇಲಿನ ನೀರು ಬಿಸಿಲಿನಲ್ಲಿ ಆವಿಯಾಗುತ್ತದೆ.',
    subject: 'Science',
  },
  {
    id: 's3',
    english: 'Gravity',
    kannada: 'ಗುರುತ್ವಾಕರ್ಷಣೆ',
    explanation: 'ಭೂಮಿಯು ಎಲ್ಲ ವಸ್ತುಗಳನ್ನು ತನ್ನ ಕಡೆಗೆ ಎಳೆದುಕೊಳ್ಳುವ ಶಕ್ತಿ.',
    example: 'ಮರದಿಂದ ಬಿದ್ದ ಹಣ್ಣು ನೆಲಕ್ಕೆ ಬೀಳುವುದು ಗುರುತ್ವಾಕರ್ಷಣೆಯಿಂದ.',
    subject: 'Science',
  },
  {
    id: 's4',
    english: 'Osmosis',
    kannada: 'ಆಸ್ಮೋಸಿಸ್',
    explanation: 'ನೀರು ತೆಳ್ಳನೆಯ ಪರದೆಯ ಮೂಲಕ ಕಡಿಮೆ ಸಾಂದ್ರತೆಯಿಂದ ಹೆಚ್ಚು ಸಾಂದ್ರತೆಗೆ ಚಲಿಸುವ ಪ್ರಕ್ರಿಯೆ.',
    example: 'ಸಸ್ಯದ ಬೇರುಗಳು ಮಣ್ಣಿನಿಂದ ನೀರನ್ನು ಆಸ್ಮೋಸಿಸ್‌ನಿಂದ ಹೀರಿಕೊಳ್ಳುತ್ತವೆ.',
    subject: 'Science',
  },
  {
    id: 's5',
    english: 'Respiration',
    kannada: 'ಉಸಿರಾಟ',
    explanation: 'ಜೀವಿಗಳು ಆಮ್ಲಜನಕ ತೆಗೆದುಕೊಂಡು ಶಕ್ತಿ ಉತ್ಪಾದಿಸಿ ಕಾರ್ಬನ್ ಡೈಆಕ್ಸೈಡ್ ಬಿಡುವ ಕ್ರಿಯೆ.',
    example: 'ನಾವು ಪ್ರತಿ ನಿಮಿಷ ಉಸಿರಾಟ ನಡೆಸುತ್ತೇವೆ.',
    subject: 'Science',
  },
  {
    id: 's6',
    english: 'Atom',
    kannada: 'ಪರಮಾಣು',
    explanation: 'ವಸ್ತುವಿನ ಅತ್ಯಂತ ಚಿಕ್ಕ ಭಾಗ, ಇದನ್ನು ಇನ್ನಷ್ಟು ವಿಭಜಿಸಲು ಸಾಧ್ಯವಿಲ್ಲ.',
    example: 'ಒಂದು ಹನಿ ನೀರಿನಲ್ಲಿ ಕೋಟ್ಯಂತರ ಪರಮಾಣುಗಳಿವೆ.',
    subject: 'Science',
  },
  {
    id: 's7',
    english: 'Ecosystem',
    kannada: 'ಪರಿಸರ ವ್ಯವಸ್ಥೆ',
    explanation: 'ಒಂದು ಪ್ರದೇಶದಲ್ಲಿರುವ ಜೀವಿಗಳು ಮತ್ತು ಅವುಗಳ ಸುತ್ತಲಿನ ಪ್ರಕೃತಿ ಸೇರಿ ಮಾಡುವ ವ್ಯವಸ್ಥೆ.',
    example: 'ಕಾಡು ಒಂದು ದೊಡ್ಡ ಪರಿಸರ ವ್ಯವಸ್ಥೆಯಾಗಿದೆ.',
    subject: 'Science',
  },
  {
    id: 's8',
    english: 'Refraction',
    kannada: 'ವಕ್ರೀಭವನ',
    explanation: 'ಬೆಳಕು ಒಂದು ಮಾಧ್ಯಮದಿಂದ ಇನ್ನೊಂದಕ್ಕೆ ಹಾದು ಹೋಗುವಾಗ ಬಾಗುವ ಗುಣ.',
    example: 'ನೀರಿನಲ್ಲಿ ಇಟ್ಟ ಬೆರಳು ಬಾಗಿದಂತೆ ಕಾಣುವುದು ವಕ್ರೀಭವನದಿಂದ.',
    subject: 'Science',
  },
  // Mathematics
  {
    id: 'm1',
    english: 'Algebra',
    kannada: 'ಬೀಜಗಣಿತ',
    explanation: 'ಅಂಕಿಗಳ ಬದಲಾಗಿ ಅಕ್ಷರಗಳನ್ನು ಬಳಸಿ ಲೆಕ್ಕ ಮಾಡುವ ಗಣಿತ ಶಾಖೆ.',
    example: 'x + 5 = 10 ಎಂಬುದು ಬೀಜಗಣಿತದ ಸಮೀಕರಣ.',
    subject: 'Mathematics',
  },
  {
    id: 'm2',
    english: 'Fraction',
    kannada: 'ಭಿನ್ನರಾಶಿ',
    explanation: 'ಒಂದು ಸಂಖ್ಯೆಯನ್ನು ಭಾಗಗಳಾಗಿ ವಿಂಗಡಿಸಿ ತೋರಿಸುವ ರೀತಿ.',
    example: '½ ಎಂದರೆ ಒಂದರ ಅರ್ಧ ಭಾಗ.',
    subject: 'Mathematics',
  },
  {
    id: 'm3',
    english: 'Circumference',
    kannada: 'ಪರಿಧಿ',
    explanation: 'ವೃತ್ತದ ಸುತ್ತಲಿನ ಅಂಚಿನ ಉದ್ದ.',
    example: 'ಚಕ್ರದ ಸುತ್ತಳತೆಯನ್ನು ಪರಿಧಿ ಎಂದು ಕರೆಯುತ್ತಾರೆ.',
    subject: 'Mathematics',
  },
  {
    id: 'm4',
    english: 'Perimeter',
    kannada: 'ಪರಿಮಿತಿ',
    explanation: 'ಯಾವುದೇ ಆಕಾರದ ಎಲ್ಲ ಬದಿಗಳ ಉದ್ದವನ್ನು ಕೂಡಿಸಿದ ಒಟ್ಟು ಉದ್ದ.',
    example: '4 ಮೀ ಬದಿಯ ಚೌಕದ ಪರಿಮಿತಿ 16 ಮೀ.',
    subject: 'Mathematics',
  },
  {
    id: 'm5',
    english: 'Probability',
    kannada: 'ಸಂಭಾವ್ಯತೆ',
    explanation: 'ಒಂದು ಘಟನೆ ಎಷ್ಟು ಸಾಧ್ಯತೆಯಲ್ಲಿ ನಡೆಯಬಹುದು ಎಂಬ ಅಳತೆ.',
    example: 'ನಾಣ್ಯ ಎಸೆದರೆ ತಲೆ ಬರುವ ಸಂಭಾವ್ಯತೆ ½.',
    subject: 'Mathematics',
  },
  {
    id: 'm6',
    english: 'Symmetry',
    kannada: 'ಸಮರೂಪತೆ',
    explanation: 'ಒಂದು ಆಕಾರವನ್ನು ಅರ್ಧಕ್ಕೆ ಮಡಿಸಿದಾಗ ಎರಡೂ ಭಾಗ ಒಂದೇ ರೀತಿ ಇರುವ ಗುಣ.',
    example: 'ಚಿಟ್ಟೆಯ ರೆಕ್ಕೆಗಳು ಸಮರೂಪತೆಯನ್ನು ತೋರಿಸುತ್ತವೆ.',
    subject: 'Mathematics',
  },
  {
    id: 'm7',
    english: 'Integer',
    kannada: 'ಪೂರ್ಣಾಂಕ',
    explanation: 'ದಶಾಂಶ ಇಲ್ಲದ ಸಂಪೂರ್ಣ ಸಂಖ್ಯೆ, ಋಣ ಅಥವಾ ಧನ ಯಾವುದೂ ಆಗಬಹುದು.',
    example: '-3, 0, 5 ಇವು ಪೂರ್ಣಾಂಕಗಳು.',
    subject: 'Mathematics',
  },
  {
    id: 'm8',
    english: 'Ratio',
    kannada: 'ಅನುಪಾತ',
    explanation: 'ಎರಡು ಸಂಖ್ಯೆಗಳ ನಡುವಿನ ಹೋಲಿಕೆಯನ್ನು ತೋರಿಸುವ ರೀತಿ.',
    example: 'ತರಗತಿಯಲ್ಲಿ ಹುಡುಗರು ಮತ್ತು ಹುಡುಗಿಯರ ಅನುಪಾತ 3:2.',
    subject: 'Mathematics',
  },
  // Commerce
  {
    id: 'c1',
    english: 'Investment',
    kannada: 'ಹೂಡಿಕೆ',
    explanation: 'ಲಾಭ ಪಡೆಯುವ ಉದ್ದೇಶದಿಂದ ಹಣ ಅಥವಾ ಸಂಪನ್ಮೂಲ ಮೀಸಲಿಡುವ ಕ್ರಿಯೆ.',
    example: 'ಬ್ಯಾಂಕಿನಲ್ಲಿ ಹಣ ಇಡುವುದು ಒಂದು ಬಗೆಯ ಹೂಡಿಕೆ.',
    subject: 'Commerce',
  },
  {
    id: 'c2',
    english: 'Inflation',
    kannada: 'ಹಣದುಬ್ಬರ',
    explanation: 'ಕಾಲಾಂತರದಲ್ಲಿ ವಸ್ತುಗಳ ಬೆಲೆ ಏರುತ್ತ ಹೋಗಿ ಹಣದ ಮೌಲ್ಯ ಕಡಿಮೆಯಾಗುವ ಪ್ರಕ್ರಿಯೆ.',
    example: '10 ವರ್ಷ ಹಿಂದೆ ₹10ಕ್ಕೆ ಸಿಗುತ್ತಿದ್ದ ವಸ್ತು ಈಗ ₹20 ಆಗಿದೆ.',
    subject: 'Commerce',
  },
  {
    id: 'c3',
    english: 'Profit',
    kannada: 'ಲಾಭ',
    explanation: 'ಒಂದು ವ್ಯಾಪಾರದಲ್ಲಿ ವೆಚ್ಚಕ್ಕಿಂತ ಹೆಚ್ಚು ಆದಾಯ ಬಂದಾಗ ಸಿಗುವ ಹೆಚ್ಚಿನ ಮೊತ್ತ.',
    example: '₹100ಕ್ಕೆ ಕೊಂಡ ಪುಸ್ತಕ ₹130ಕ್ಕೆ ಮಾರಿದರೆ ₹30 ಲಾಭ.',
    subject: 'Commerce',
  },
  {
    id: 'c4',
    english: 'Balance Sheet',
    kannada: 'ತುಲನ ಪಟ್ಟಿ',
    explanation: 'ಒಂದು ಕಂಪನಿಯ ಆಸ್ತಿ, ಸಾಲ ಮತ್ತು ಮಾಲೀಕರ ಬಂಡವಾಳ ತೋರಿಸುವ ಹಣಕಾಸು ದಾಖಲೆ.',
    example: 'ಪ್ರತಿ ವರ್ಷ ಕಂಪನಿ ತನ್ನ ತುಲನ ಪಟ್ಟಿ ಪ್ರಕಟಿಸುತ್ತದೆ.',
    subject: 'Commerce',
  },
  {
    id: 'c5',
    english: 'Interest',
    kannada: 'ಬಡ್ಡಿ',
    explanation: 'ಸಾಲ ತೆಗೆದುಕೊಂಡ ಹಣಕ್ಕೆ ನಿರ್ದಿಷ್ಟ ಅವಧಿಗೆ ಪಾವತಿಸಬೇಕಾದ ಹೆಚ್ಚಿನ ಮೊತ್ತ.',
    example: 'ಬ್ಯಾಂಕಿನಿಂದ ₹1000 ಸಾಲ ತೆಗೆದರೆ ₹100 ಬಡ್ಡಿ ನೀಡಬೇಕು.',
    subject: 'Commerce',
  },
  {
    id: 'c6',
    english: 'Demand',
    kannada: 'ಬೇಡಿಕೆ',
    explanation: 'ಖರೀದಿದಾರರು ಒಂದು ನಿರ್ದಿಷ್ಟ ಬೆಲೆಗೆ ಕೊಳ್ಳಲು ಸಿದ್ಧರಿರುವ ವಸ್ತುವಿನ ಪ್ರಮಾಣ.',
    example: 'ಮಳೆಗಾಲದಲ್ಲಿ ಛತ್ರಿಯ ಬೇಡಿಕೆ ಹೆಚ್ಚಾಗುತ್ತದೆ.',
    subject: 'Commerce',
  },
  {
    id: 'c7',
    english: 'Tax',
    kannada: 'ತೆರಿಗೆ',
    explanation: 'ಸರ್ಕಾರ ನಾಗರಿಕರಿಂದ ಮತ್ತು ವ್ಯವಹಾರಗಳಿಂದ ಕಡ್ಡಾಯವಾಗಿ ವಸೂಲಿಸುವ ಹಣ.',
    example: 'ಮನೆ ಕೊಂಡಾಗ ನೋಂದಣಿ ತೆರಿಗೆ ಕಟ್ಟಬೇಕು.',
    subject: 'Commerce',
  },
  {
    id: 'c8',
    english: 'Dividend',
    kannada: 'ಲಾಭಾಂಶ',
    explanation: 'ಕಂಪನಿ ತನ್ನ ಷೇರುದಾರರಿಗೆ ಲಾಭದ ಒಂದು ಭಾಗವನ್ನು ಹಂಚುವ ಮೊತ್ತ.',
    example: 'ಕಂಪನಿ ತನ್ನ ಷೇರುದಾರರಿಗೆ ಪ್ರತಿ ವರ್ಷ ಲಾಭಾಂಶ ನೀಡಿತು.',
    subject: 'Commerce',
  },
];

export function getWordOfTheDay(): Word {
  const index = new Date().getDate() % words.length;
  return words[index];
}
