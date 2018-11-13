module.exports = {
  cleanRuntimeError(err) {
    const lines = err.split(/\n/);
    const firstInternal = lines.findIndex(l => l.trim().startsWith('at sun.reflect'));
    return lines.slice(0, firstInternal).join('\n');
  },
};
